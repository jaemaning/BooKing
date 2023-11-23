package com.booking.booking.post.dto.response;

import com.booking.booking.global.dto.response.MemberResponse;
import com.booking.booking.post.domain.Post;

import java.time.LocalDateTime;

public record PostDetailResponse(
        Long postId,
        Long meetingId,
        Integer memberId,
        String nickname,
        String profileImage,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public PostDetailResponse(Post post, MemberResponse member) {
        this(post.getPostId(), post.getMeetingId(), member.memberPk(), member.nickname(), member.profileImage(),
                post.getTitle(), post.getContent(), post.getCreatedAt(), post.getUpdatedAt());
    }
}
