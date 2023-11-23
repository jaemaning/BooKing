package com.booking.booking.stt.dto.response;

import com.booking.booking.stt.domain.Segment;
import com.booking.booking.stt.domain.Speaker;
import com.booking.booking.stt.domain.Transcription;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class TranscriptionResponse {
    private String id;

    private List<Segment> segments;

    private String text;

    private List<Speaker> speakers;

    private LocalDateTime createdAt;

    private String fileName;

    private long meetingInfoId;

    public TranscriptionResponse(Transcription transcription){
        this(
                transcription.getId(),
                transcription.getSegments(),
                transcription.getText(),
                transcription.getSpeakers(),
                transcription.getCreatedAt(),
                transcription.getFileName(),
                transcription.getMeetingInfoId()
        );
    }

}
