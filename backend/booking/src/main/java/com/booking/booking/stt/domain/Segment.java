package com.booking.booking.stt.domain;

import lombok.Data;

@Data
public class Segment {
    private long start;
    private long end;
    private String text;
    private String textEdited;
    private Speaker speaker;
}
