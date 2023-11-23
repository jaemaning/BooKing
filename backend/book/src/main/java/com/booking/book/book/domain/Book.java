package com.booking.book.book.domain;

import com.booking.book.global.data.LocalDateConverter;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import java.time.LocalDate;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.TextScore;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "book")
public class Book {

    @CsvBindByPosition(position = 0)
    @Id @Field("isbn")
    private String isbn;

    @CsvBindByPosition(position = 1)
    @TextIndexed @Field("title")
    private String title;

    @CsvBindByPosition(position = 2)
    @Field("author")
    private String author;

    @CsvBindByPosition(position = 3)
    @Field("coverImage")
    private String coverImage;

    @CsvBindByPosition(position = 4)
    @Field("genre")
    private String genre;

    @Field("publishDate")
    @CsvCustomBindByPosition(position = 5, converter = LocalDateConverter.class)
    private LocalDate publishDate;

    @CsvBindByPosition(position = 6)
    @Field("content")
    private String content;

    @TextScore private Float score;

    @Field("meeting_cnt")
    @Setter
    private Integer meetingCnt;

}
