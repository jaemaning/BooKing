package com.booking.booking.stt.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "summaries")
@Getter
public class Summary {
    @Id
    @Field("summary_id")
    private String id;

    @Field
    private String text;

    @Field("transcription_id")
    private String transcriptionId;

    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    public static Summary of(String text,String transcriptionId){
        return new Summary(text,transcriptionId);
    }

    public Summary (String text,String transcriptionId){
        this.text=text;
        this.transcriptionId=transcriptionId;
    }
}
