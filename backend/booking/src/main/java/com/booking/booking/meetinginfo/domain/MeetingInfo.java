package com.booking.booking.meetinginfo.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "meetinginfos")
public class MeetingInfo {
    @Id
    private long meetinginfoId;

    private Long meetingId;

    private LocalDateTime date;

    private String location;

    private String address;

    private Double lat;

    private Double lgt;

    private Integer fee;
}
