package com.booking.booking.meeting.dto.request;

import com.booking.booking.global.dto.response.MemberResponse;
import com.booking.booking.meeting.domain.Meeting;
import com.booking.booking.meeting.domain.MeetingState;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

public record MeetingRequest(
    String bookIsbn,
    String meetingTitle,
    String description,
    @Min(2) @Max(6)
    Integer maxParticipants,
    List<String> hashtagList,
    String address
) {

    public Meeting toEntity(MemberResponse member, MeetingState meetingState) {
        return Meeting.builder()
                      .leaderId(member.memberPk())
                      .lat(member.lat())
                      .lgt(member.lgt())
                      .bookIsbn(bookIsbn)
                      .meetingTitle(meetingTitle)
                      .description(description)
                      .maxParticipants(maxParticipants)
                      .meetingState(meetingState)
                      .address(address)
                      .build();
    }
}
