package com.booking.chat.mongo.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "auto_sequence")
public class DatabaseSequence {

    @Id
    private String id;
    private Long seq;
}
