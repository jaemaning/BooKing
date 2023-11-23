package com.booking.booking.meeting.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

public record MeetingUpdateRequest(
        long meetingId,
        String meetingTitle,
        String description,
        @Min(2) @Max(6)
        Integer maxParticipants,
        List<String> hashtagList
) {
}
