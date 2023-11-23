package com.booking.book.util;

import com.booking.book.book.controller.BookController;
import com.booking.book.book.repository.BookRepository;
import com.booking.book.book.service.BookService;
import com.booking.book.global.WebConfig;
import com.booking.book.global.mongo.MemberBookIncrementListener;
import com.booking.book.global.mongo.SequenceGeneratorService;
import com.booking.book.memberbook.controller.MemberBookController;
import com.booking.book.memberbook.repository.MemberBookRepository;
import com.booking.book.memberbook.service.MemberBookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureRestDocs
@ActiveProfiles("local")
@ExtendWith(RestDocumentationExtension.class)
@WebFluxTest({
    BookController.class,
    MemberBookController.class
})
@Import(WebConfig.class)
public class ControllerTest {

    @Autowired protected RestDocumentationContextProvider restDocumentation;

    @Autowired protected WebTestClient webTestClient;
    @Autowired protected ObjectMapper objectMapper;

    // Service
    @MockBean private MemberBookIncrementListener memberBookIncrementListener;
    @MockBean private SequenceGeneratorService sequenceGeneratorService;

    @MockBean protected MemberBookService memberBookService;
    @MockBean protected BookService bookService;

    // Repo
    @MockBean protected MemberBookRepository memberBookRepository;
    @MockBean protected BookRepository bookRepository;

    // Util
    @MockBean private MappingMongoConverter mappingMongoConverter;




}
