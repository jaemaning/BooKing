package com.booking.booking.meeting.controller;

import com.booking.booking.hashtag.dto.response.HashtagResponse;
import com.booking.booking.meeting.dto.request.MeetingRequest;
import com.booking.booking.meeting.dto.response.MeetingResponse;
import com.booking.booking.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MvcResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class MeetingControllerTest extends ControllerTest {

    private final String baseUrl = "/api/booking/meeting";

    @Test
    @DisplayName("모임 생성")
    void t1() throws Exception {
        List<String> hashtags = new ArrayList<String>();
        hashtags.add("hashtag");
        MeetingRequest meetingRequest = new MeetingRequest(
                "isbn",
                "book title",
                "description",
                5,
                hashtags
        );


        when(meetingService.arrangeMeeting(anyString(), any())).thenReturn(Mono.empty());

        MvcResult mvcResult = mockMvc.perform(post(baseUrl + "/")
                        .header("Authorization", "Bearer JWT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(meetingRequest)))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNoContent())
                .andDo(
                        document("meeting/create",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("bookIsbn").description("책 isbn"),
                                        fieldWithPath("meetingTitle").description("모임명"),
                                        fieldWithPath("description").description("모임 소개"),
                                        fieldWithPath("maxParticipants").description("모임 최대 인원"),
                                        fieldWithPath("hashtagList").description("해시태그").optional().type(JsonFieldType.ARRAY)
                                ))
                );
    }

    @Test
    @DisplayName("모임 단일 조회")
    void t2() throws Exception {
        HashtagResponse hashtagResponse = new HashtagResponse(
                1,
                "해시태그"
        );
        MeetingResponse meetingResponse = new MeetingResponse(
                "isbn",
                "모임명",
                "모임소개",
                5,
                new ArrayList<>(Arrays.asList(hashtagResponse))
        );

        when(meetingService.findById(any())).thenReturn(Mono.just(meetingResponse));

        MvcResult mvcResult = mockMvc.perform(get(baseUrl + "/{meetingId}", 1)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andDo(
                        document("meeting/findById",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("bookIsbn").description("책 isbn"),
                                        fieldWithPath("meetingTitle").description("모임명"),
                                        fieldWithPath("description").description("모임 소개"),
                                        fieldWithPath("maxParticipants").description("최대 인원"),
                                        subsectionWithPath("hashtagList").description("해시태그 목록").type(JsonFieldType.ARRAY),
                                        fieldWithPath("hashtagList[].hashtagId").description("해시태그 ID").type(JsonFieldType.NUMBER),
                                        fieldWithPath("hashtagList[].content").description("해시태그 내용").type(JsonFieldType.STRING)
                                )
                        )
                );
    }

    @Test
    @DisplayName("모임 전체 조회")
    void t3() throws Exception {
        HashtagResponse hashtagResponse1 = new HashtagResponse(
                1,
                "해시태그1"
        );

        HashtagResponse hashtagResponse2 = new HashtagResponse(
                2,
                "해시태그2"
        );
        List<MeetingResponse> mockResponses = new ArrayList<>();
        mockResponses.add(new MeetingResponse("isbn",
                "모임명",
                "모임소개",
                5,
                new ArrayList<>(Arrays.asList(hashtagResponse1))));
        mockResponses.add(new MeetingResponse("isbn",
                "모임명",
                "모임소개",
                5,
                new ArrayList<>(Arrays.asList(hashtagResponse2))));
        Flux<MeetingResponse> f = Flux.fromIterable(mockResponses);
        when(meetingService.findAllByLocation()).thenReturn(f);

        FieldDescriptor[] meetingResponseFields = new FieldDescriptor[]{
                fieldWithPath("[]").description("모임 목록"),
                fieldWithPath("[].bookIsbn").description("책 ISBN 번호"),
                fieldWithPath("[].meetingTitle").description("모임 제목"),
                fieldWithPath("[].description").description("모임 설명"),
                fieldWithPath("[].maxParticipants").description("최대 참여 인원"),
                fieldWithPath("[].hashtagList").description("해시태그 목록"),
                fieldWithPath("[].hashtagList[].hashtagId").description("해시태그 ID"),
                fieldWithPath("[].hashtagList[].content").description("해시태그 내용")
        };
        MvcResult mvcResult = mockMvc.perform(get(baseUrl + "/")
                .header("Authorization", "Bearer JWT")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andDo(document("meeting/findAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                meetingResponseFields
                        )));
    }
}
