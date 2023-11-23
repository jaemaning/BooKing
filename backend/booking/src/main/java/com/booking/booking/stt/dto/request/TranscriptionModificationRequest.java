package com.booking.booking.stt.dto.request;

import com.booking.booking.stt.domain.Segment;
import com.booking.booking.stt.domain.Speaker;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TranscriptionModificationRequest {
    private String id;

    private List<Segment> segments;

    private String text;

    private List<Speaker> speakers;

    private LocalDateTime createdAt;

    private String fileName;
}
