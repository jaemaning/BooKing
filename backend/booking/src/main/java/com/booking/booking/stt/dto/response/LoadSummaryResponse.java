package com.booking.booking.stt.dto.response;

import com.booking.booking.stt.domain.Summary;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LoadSummaryResponse {
    private String id;

    private String text;

    private String transcriptionId;

    private LocalDateTime createdAt;

    public LoadSummaryResponse (Summary summary) {
        this(
                summary.getId(),
                summary.getText(),
                summary.getTranscriptionId(),
                summary.getCreatedAt()
        );
    }

}
