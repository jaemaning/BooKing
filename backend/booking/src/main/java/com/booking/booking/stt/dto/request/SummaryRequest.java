package com.booking.booking.stt.dto.request;

import com.booking.booking.stt.dto.SummaryDocument;
import com.booking.booking.stt.dto.SummaryOption;
import lombok.Data;

@Data
public class SummaryRequest {
    private SummaryDocument document;
    private SummaryOption option;

    public SummaryRequest(String content) {
        this.document=new SummaryDocument(content);
        this.option=new SummaryOption();
    }
}
