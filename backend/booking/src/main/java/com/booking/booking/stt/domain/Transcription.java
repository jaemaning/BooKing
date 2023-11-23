package com.booking.booking.stt.domain;

import com.booking.booking.stt.dto.request.TranscriptionModificationRequest;
import com.booking.booking.stt.dto.response.SttResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Document(collection = "transcriptions")
public class Transcription {

    @Id
    private String id;

    @Field("segments")
    @Setter
    private List<Segment> segments;

    @Field("text")
    @Setter
    private String text;

    @Field("speakers")
    @Setter
    private List<Speaker> speakers;

    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Field("filename")
    @Setter
    private String fileName;

    @Field("meeting_info_id")
    @Setter
    private long meetingInfoId;

    public static Transcription of(SttResponseDto dto,String fileName,long meetingInfoId) {
        Transcription transcription = new Transcription();
        transcription.segments=dto.getSegments();
        transcription.speakers=dto.getSpeakers();
//        transcription.text=dto.getText();
        transcription.text = dto.getSegments().stream()
                .map(Segment::getText)
                .filter(Objects::nonNull) // null이 아닌 text 필드만 필터링
                .collect(Collectors.joining("\n"));
        transcription.fileName=fileName;
        transcription.meetingInfoId=meetingInfoId;
        return transcription;
    }

    public void setExcludeId(TranscriptionModificationRequest request) {
        this.setSegments(request.getSegments());
        this.setText(request.getText());
        this.setSpeakers(request.getSpeakers());
        this.setFileName(request.getFileName());
    }
}
