package com.booking.booking.post.dto.request;

public record PostUpdateRequest(
        Long postId,
        Long meetingId,
        String title,
        String content
) {
}
