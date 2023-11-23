package com.booking.booking.meetinginfo.dto.request;

import com.booking.booking.meetinginfo.domain.MeetingInfo;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record MeetingInfoRequest(
        long meetingId,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime date,
        String location,
        String address,
        Double lat,
        Double lgt,
        Integer fee
) {
    public MeetingInfo toEntity() {
        return MeetingInfo.builder()
                .meetingId(meetingId)
                .date(date)
                .location(location)
                .address(address)
                .lat(lat)
                .lgt(lgt)
                .fee(fee)
                .build();
    }
}