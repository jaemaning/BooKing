package com.booking.booking.post.dto.request;

import com.booking.booking.post.domain.Post;

public record PostRequest(
        Long meetingId,
        String title,
        String content
) {
    public Post toEntity(Integer memberId) {
        return Post.builder()
                .meetingId(meetingId)
                .memberId(memberId)
                .title(title)
                .content(content)
                .build();
    }
}
