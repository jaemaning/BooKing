package com.booking.booking.stt.dto;

import lombok.Data;

@Data
public class SummaryOption {
    private final String language="ko";
    private final String model="news"; //general or news
    private final Integer tone=3; // 1 해요체 2 정중체 3 종결체
    private final Integer summaryCount=2; //요약 문장 수 default 3
}
