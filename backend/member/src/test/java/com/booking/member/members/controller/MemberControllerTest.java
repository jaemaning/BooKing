package com.booking.member.members.controller;

import com.booking.member.members.dto.*;
import com.booking.member.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerTest extends ControllerTest {

    private final String baseUrl = "/api/members";

    @Test
    @DisplayName("회원 가입 요청 성공한다")
    void t1() throws Exception {

        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("loginId", "email", 20, "MALE", "nickname", "fullName", "address","profileImage","provider");
        when(memberService.signup(any(SignUpRequestDto.class)))
                .thenReturn(Mono.just("token:JWT 토큰"));

        MvcResult mvcResult = mockMvc.perform(post(baseUrl + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(signUpRequestDto)))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk()) // 200 반환
                .andExpect(content().string("token:JWT 토큰"))
                .andDo(
                        document("/member/signup", // restdocs 선언,
                                preprocessRequest(prettyPrint()), // json을 이쁘게 표시해라
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인id"),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("age").description("나이"),
                                        fieldWithPath("gender").description("성별, 남자 : MALE, 여자 : FEMALE"),
                                        fieldWithPath("nickname").description("닉네임"),
                                        fieldWithPath("fullName").description("풀네임"),
                                        fieldWithPath("address").description("주소"),
                                        fieldWithPath("profileImage").description("프로필 이미지"),
                                        fieldWithPath("provider").description("google | kakao")
                                ))
                );
    }

    @Test
    @DisplayName("회원 정보를 조회한다.")
    void t2() throws Exception {
        String loginId = "1234";
        MemberInfoResponseDto responseDto = new MemberInfoResponseDto("1234", "email", 10, "MALE",
                "mono", "monono", 1.1, 1.1, "profileImg","google",1,100000);

        when(memberService.loadMemberInfo(any())).thenReturn(Mono.just(responseDto));

        MvcResult mvcResult = mockMvc.perform(get(baseUrl + "/memberInfo/{loginId}", loginId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk()) // 200 반환
                .andDo(
                        document("/member/info",
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("loginId").description("로그인 id")
                                ),
                                responseFields(
                                        fieldWithPath("loginId").description("로그인 id"),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("age").description("나이"),
                                        fieldWithPath("gender").description("성별"),
                                        fieldWithPath("nickname").description("닉네임"),
                                        fieldWithPath("fullname").description("이름"),
                                        fieldWithPath("lat").description("위도"),
                                        fieldWithPath("lgt").description("경도"),
                                        fieldWithPath("profileImage").description("프로필 이미지"),
                                        fieldWithPath("provider").description("google,kakao"),
                                        fieldWithPath("memberPk").description("member pk"),
                                        fieldWithPath("point").description("잔여 포인트")
                                ))

                );
    }

    @Test
    @WithMockUser
    @DisplayName("회원 정보 수정")
    void t3() throws Exception {
        ModifyRequestDto modifyRequestDto = new ModifyRequestDto("123", "mono", "img");
        when(memberService.modifyMemberInfo(any(ModifyRequestDto.class))).thenReturn(Mono.empty());

        mockMvc.perform(patch(baseUrl + "/modification")
                        .header("Authorization", "Bearer JWT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(modifyRequestDto)))
                .andExpect(status().isOk())
                .andDo(
                        document("/member/modify",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("loginId").description("로그인 id"),
                                        fieldWithPath("nickname").description("닉네임"),
                                        fieldWithPath("profileImage").description("프로필 이미지")
                                )
                        )

                );
    }

    @Test
    @DisplayName("회원 탈퇴")
    void t4() throws Exception {
        DeleteMemberRequestDto deleteMemberRequestDto = new DeleteMemberRequestDto("1234");
        when(memberService.deleteMember(deleteMemberRequestDto.loginId())).thenReturn(Mono.empty());

        mockMvc.perform(delete(baseUrl + "/deletion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(deleteMemberRequestDto)))
                .andExpect(status().isOk())
                .andDo(
                        document("/member/delete",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("loginId").description("로그인id")
                                ))
                );
    }

    @Test
    @DisplayName("로그인 JWT")
    void t5() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("1234");
        when(memberService.login(anyString())).thenReturn(Mono.just("token: JWT 토큰"));

        MvcResult mvcResult = mockMvc.perform(post(baseUrl + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(loginRequestDto))
                ).andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string("token: JWT 토큰"))
                .andDo(
                        document("/member/login", // restdocs 선언,
                                preprocessRequest(prettyPrint()), // json을 이쁘게 표시해라
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인id")
                                ))
                );
    }

    @Test
    @WithMockUser
    @DisplayName("회원 위치 정보 수정")
    void t6() throws Exception {
        ChangeLocationRequestDto changeLocationRequestDto=new ChangeLocationRequestDto("addr");
        when(memberService.changeLocation(any(),anyString())).thenReturn(Mono.empty());

        mockMvc.perform(patch(baseUrl + "/location")
                        .header("Authorization", "Bearer JWT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(changeLocationRequestDto)))
                .andExpect(status().isOk())
                .andDo(
                        document("/member/location",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("address").description("위치")
                                )
                        )

                );
    }

    @Test
    @DisplayName("회원 PK로 조회")
    void t7() throws Exception {
        Integer memberPk = 1;
        MemberInfoResponseDto responseDto = new MemberInfoResponseDto("1234", "email", 10, "MALE",
                "mono", "monono", 1.1, 1.1, "profileImg","google",1,100000);

        when(memberService.loadMemberInfoByPk(anyInt())).thenReturn(Mono.just(responseDto));

        MvcResult mvcResult = mockMvc.perform(get(baseUrl + "/memberInfo-pk/{memberPk}", memberPk)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk()) // 200 반환
                .andDo(
                        document("/member/info-pk",
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("memberPk").description("pk: Integer")
                                ),
                                responseFields(
                                        fieldWithPath("loginId").description("로그인 id"),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("age").description("나이"),
                                        fieldWithPath("gender").description("성별"),
                                        fieldWithPath("nickname").description("닉네임"),
                                        fieldWithPath("fullname").description("이름"),
                                        fieldWithPath("lat").description("위도"),
                                        fieldWithPath("lgt").description("경도"),
                                        fieldWithPath("profileImage").description("프로필 이미지"),
                                        fieldWithPath("provider").description("google,kakao"),
                                        fieldWithPath("memberPk").description("member pk"),
                                        fieldWithPath("point").description("잔여 포인트")
                                ))

                );
    }

    @Test
    @DisplayName("회원 닉네임으로 조회")
    void t8() throws Exception {
        String nickname= "닉네임";
        MemberInfoResponseDto responseDto = new MemberInfoResponseDto("1234", "email", 10, "MALE",
                "mono", "monono", 1.1, 1.1, "profileImg","google",1,100000);

        when(memberService.loadMemberInfoByNickname(anyString())).thenReturn(Mono.just(responseDto));

        MvcResult mvcResult = mockMvc.perform(get(baseUrl + "/memberInfo-nick/{nickname}", nickname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk()) // 200 반환
                .andDo(
                        document("/member/info-nick",
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("nickname").description("닉네임: string")
                                ),
                                responseFields(
                                        fieldWithPath("loginId").description("로그인 id"),
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("age").description("나이"),
                                        fieldWithPath("gender").description("성별"),
                                        fieldWithPath("nickname").description("닉네임"),
                                        fieldWithPath("fullname").description("이름"),
                                        fieldWithPath("lat").description("위도"),
                                        fieldWithPath("lgt").description("경도"),
                                        fieldWithPath("profileImage").description("프로필 이미지"),
                                        fieldWithPath("provider").description("google,kakao"),
                                        fieldWithPath("memberPk").description("member pk"),
                                        fieldWithPath("point").description("잔여 포인트")
                                ))

                );
    }
}
