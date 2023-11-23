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
public class FollowingsResponseDto {
    private List<Following> followings;
    private Integer followingsCnt;

    @AllArgsConstructor
    @Data
    public static class Following{
        private Integer memberPk;
        private String nickname;
        private String profileImage;
    }
}
