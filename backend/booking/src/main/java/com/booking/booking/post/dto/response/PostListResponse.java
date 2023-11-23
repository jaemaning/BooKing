package com.booking.booking.post.dto.response;

import com.booking.booking.global.dto.response.MemberResponse;
import com.booking.booking.post.domain.Post;

import java.time.LocalDateTime;

public record PostListResponse(
        Long postId,
        Long meetingId,
        Integer memberId,
        String nickname,
        String profileImage,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public PostListResponse(Post post, MemberResponse member) {
        this(post.getPostId(), post.getMeetingId(), member.memberPk(), member.nickname(), member.profileImage(),
                post.getTitle(), post.getCreatedAt(), post.getUpdatedAt());
    }
}
