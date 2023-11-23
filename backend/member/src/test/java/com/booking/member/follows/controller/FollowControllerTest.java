package com.booking.member.follows.controller;

import com.booking.member.follows.dto.FollowersResponseDto;
import com.booking.member.follows.dto.FollowersResponseDto.Follower;
import com.booking.member.follows.dto.FollowingsResponseDto;
import com.booking.member.follows.dto.FollowingsResponseDto.Following;
import com.booking.member.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

public class FollowControllerTest extends ControllerTest {

    private final String baseUrl = "/api/follows";

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("팔로우")
    void t1() throws Exception {

        when(followService.follow(anyString(), anyString())).thenReturn(Mono.empty());
        String nickname = "nick";
        MvcResult mvcResult = mockMvc.perform(post(baseUrl + "/{nickname}", nickname)
                        .header("Authorization", "Bearer JWT"))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andDo(
                        document("follow/follow",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("nickname").description("닉네임"))
                        )
                );
    }

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("언팔로우")
    void t2() throws Exception {

        when(followService.unfollow(anyString(), anyString())).thenReturn(Mono.empty());
        String nickname = "nick";
        MvcResult mvcResult = mockMvc.perform(delete(baseUrl + "/{nickname}", nickname)
                        .header("Authorization", "Bearer JWT"))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andDo(
                        document("follow/unfollow",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("nickname").description("닉네임"))
                        )
                );
    }

    @Test
    @DisplayName("팔로워 조회")
    void t3() throws Exception {
        FollowersResponseDto followersResponseDto = FollowersResponseDto.builder()
                .followers(new ArrayList<>(Arrays.asList(
                        new Follower("nick1", "img1"),
                        new Follower("nick2", "img2")
                )))
                .followersCnt(2)
                .build();

        when(followService.getFollowers(anyString())).thenReturn(Mono.just(followersResponseDto));

        String nickname = "nick";
        MvcResult mvcResult = mockMvc.perform(
                get(baseUrl + "/followers/{nickname}", nickname)
        ).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andDo(
                        document("follow/getFollowers",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("nickname").description("닉네임")),
                                responseFields(
                                        fieldWithPath("followers").description("팔로워 리스트"),
                                        fieldWithPath("followers[].nickname").description("팔로워 닉네임"),
                                        fieldWithPath("followers[].profileImage").description("팔로워 프로필 이미지"),
                                        fieldWithPath("followersCnt").description("팔로워 수")
                                )
                        )
                );
    }

    @Test
    @DisplayName("팔로잉 조회")
    void t4() throws Exception {
        FollowingsResponseDto followingsResponseDto = FollowingsResponseDto.builder()
                .followings(new ArrayList<>(Arrays.asList(
                        new Following("nick1", "img1"),
                        new Following("nick2", "img2")
                )))
                .followingsCnt(2)
                .build();

        when(followService.getFollowings(anyString())).thenReturn(Mono.just(followingsResponseDto));

        String nickname = "nick";
        MvcResult mvcResult = mockMvc.perform(get(baseUrl + "/followings/{nickname}", nickname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andDo(
                        document("follow/getFollowings",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("nickname").description("닉네임")),
                                responseFields(
                                        fieldWithPath("followings").description("팔로잉 리스트"),
                                        fieldWithPath("followings[].nickname").description("팔로잉 닉네임"),
                                        fieldWithPath("followings[].profileImage").description("팔로잉 프로필 이미지"),
                                        fieldWithPath("followingsCnt").description("팔로잉 수")
                                )
                        )
                );
    }
}
