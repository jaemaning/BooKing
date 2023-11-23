package com.booking.book.memberbook.domain;

import com.booking.book.memberbook.dto.request.MemberBookRegistRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "member_books")
public class MemberBook {

    @Id
    private String _id;

    @Field
    private Integer memberPk;

    @Field
    private String bookIsbn;

    @Field
    private List<Note> notes;

    @Field
    @CreatedDate
    private LocalDateTime createdAt;

    public static MemberBook from(MemberBookRegistRequest memberBookRegistRequest) {

       return MemberBook.builder()
           .bookIsbn(memberBookRegistRequest.bookIsbn())
           .memberPk(memberBookRegistRequest.memberPk())
           .notes(new ArrayList<>())
           .build();
    }

//    public void setAutoIncrementId(Long id) {
//        this._id = id;
//    }
}
