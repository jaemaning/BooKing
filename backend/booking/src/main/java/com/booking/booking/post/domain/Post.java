package com.booking.booking.post.domain;

import com.booking.booking.post.dto.request.PostUpdateRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table("posts")
public class Post {
    @Id
    private long postId;

    private Long meetingId;

    private Integer memberId;

    private String title;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Post update(PostUpdateRequest postUpdateRequest) {
        return Post.builder()
                .postId(postId)
                .meetingId(meetingId)
                .memberId(memberId)
                .title(postUpdateRequest.title())
                .content(postUpdateRequest.content())
                .createdAt(createdAt)
                .build();
    }
}
