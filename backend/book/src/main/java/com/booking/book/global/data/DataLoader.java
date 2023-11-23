package com.booking.book.global.data;

import com.booking.book.book.domain.Book;
import com.booking.book.book.service.BookService;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataLoader {

    private final BookService bookService;

    public Flux<Book> readBooksFromCSV(Reader reader) throws IOException {
        ColumnPositionMappingStrategy<Book> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(Book.class);

        String[] bookFieldsToBindTo = {"isbn", "title", "author", "coverImage", "genre","publishdate" ,"content"};
        strategy.setColumnMapping(bookFieldsToBindTo);

        CsvToBean<Book> csvToBean = new CsvToBeanBuilder<Book>(reader).withMappingStrategy(strategy)
                                                                      .withSkipLines(1)
                                                                      .withType(Book.class)
                                                                      .build();
        return Flux.fromIterable(csvToBean.parse());
    }

    @Bean
    public CommandLineRunner dataLoad() {
        return (args) -> {
            bookService.ensureTextIndexOnTitle()
                       .then(bookService.initializeCheck())
                       .flatMap(exists -> {
                           if (exists) {
                               return Mono.empty();
                           } else {
                               ClassPathResource resource = new ClassPathResource("data/book_data.csv");
                               try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                                   List<Book> bookList = readBooksFromCSV(reader).collectList().block();
                                   log.info("BookInitializer start by block thread");
                                   return bookService.initializeSave(bookList);
                               } catch (IOException e) {
                                   return Mono.error(e);
                               }
                           }
                       })
                       .subscribeOn(Schedulers.boundedElastic())
                       .subscribe(
                           null,
                           e -> log.error("Data loading failed", e),
                           () -> log.info("Data loading completed successfully")
                       );
        };
    }

}
