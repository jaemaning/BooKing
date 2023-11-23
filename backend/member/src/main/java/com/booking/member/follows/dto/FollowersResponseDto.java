package com.booking.member.follows.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowersResponseDto {
    private List<Follower> followers;
    private Integer followersCnt;

    @AllArgsConstructor
    @Data
    public static class Follower{
        private Integer memberPk;
        private String nickname;
        private String profileImage;
    }
}
