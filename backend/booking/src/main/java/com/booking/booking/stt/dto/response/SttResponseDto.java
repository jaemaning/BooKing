package com.booking.booking.stt.dto.response;

import com.booking.booking.stt.domain.Segment;
import com.booking.booking.stt.domain.Speaker;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class SttResponseDto {
    private String result;
    private String message;
    private String token;
    private String version;
    private Params params;
    private int progress;
    private Map<String, List<String>> keywords;
    private List<Segment> segments;
    private String text;
    private double confidence;
    private List<Speaker> speakers;

    @Data
    public static class Params {
        private String service;
        private String domain;
        private String lang;
        private String completion;
        private Diarization diarization;
        private List<String> boostings;
        private String forbiddens;
        private boolean wordAlignment;
        private boolean fullText;
        private boolean noiseFiltering;
        private boolean resultToObs;
        private int priority;
        private UserData userdata;

    }

    @Data
    public static class Diarization {
        private boolean enable;
        private int speakerCountMin;
        private int speakerCountMax;

    }

    @Data
    public static class UserData {
        @JsonProperty("_ncp_DomainCode")
        private String ncpDomainCode;

        @JsonProperty("_ncp_DomainId")
        private int ncpDomainId;

        @JsonProperty("_ncp_TaskId")
        private int ncpTaskId;

        @JsonProperty("_ncp_TraceId")
        private String ncpTraceId;

    }

//    @Data
//    public static class Segment {
//        private int start;
//        private int end;
//        private String text;
//        private double confidence;
//        private Diarization diarization;
//        private Speaker speaker;
//        private List<List<Object>> words; // This could be a list of lists or a custom class if more structure is needed
//        private String textEdited;
//    }

//    @Data
//    public static class Speaker {
//        private String label;
//        private String name;
//        private boolean edited;
//
//    }
}










