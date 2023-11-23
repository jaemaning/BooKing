package com.booking.booking.hashtagmeeting.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "hashtag_meeting")
public class HashtagMeeting {
    @Id
    private long meetingHashtagId;

    private Long meetingId;

    private Long hashtagId;
}
