package com.booking.member.payments.controller;

import com.booking.member.payments.dto.ApprovalResponseDto;
import com.booking.member.payments.dto.ApprovalResponseDto.Amount;
import com.booking.member.payments.dto.ApprovalResponseDto.CardInfo;
import com.booking.member.payments.dto.ReadyPaymentRequestDto;
import com.booking.member.payments.dto.ReadyPaymentResponseDto;
import com.booking.member.payments.dto.SendRequestDto;
import com.booking.member.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PaymentControllerTest extends ControllerTest {

    private final String baseUrl = "/api/payments";

    @Test
    @DisplayName("결제 준비 요청")
    @WithMockUser(username = "testUser")
    void t1() throws Exception {
        ReadyPaymentResponseDto responseDto = ReadyPaymentResponseDto.builder()
                .tid("tid")
                .next_redirect_app_url("url")
                .next_redirect_mobile_url("url")
                .next_redirect_pc_url("url")
                .android_app_scheme("url")
                .ios_app_scheme("url")
                .created_at(LocalDateTime.now())
                .build();
//        ReadyPaymentResponseDto responseDto = new ReadyPaymentResponseDto("tid", "url", "url", "url", "url", "url", LocalDateTime.now());

        ReadyPaymentRequestDto readyPaymentRequestDto = new ReadyPaymentRequestDto("1000");

        when(paymentService.readyPayment(any(), anyString()))
                .thenReturn(Mono.just(responseDto));

        MvcResult mvcResult = mockMvc.perform(post(baseUrl + "/ready")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer JWT")
                        .content(objectMapper.writeValueAsBytes(readyPaymentRequestDto)))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andDo(
                        document("/payment/ready",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("amount").description("금액")
                                ),
                                responseFields(
                                        fieldWithPath("tid").description("결제 승인 요청 때 필요한 tid"),
                                        fieldWithPath("next_redirect_app_url").description("redirect url"),
                                        fieldWithPath("next_redirect_mobile_url").description("redirect url"),
                                        fieldWithPath("next_redirect_pc_url").description("redirect url"),
                                        fieldWithPath("android_app_scheme").description("redirect url"),
                                        fieldWithPath("ios_app_scheme").description("redirect url"),
                                        fieldWithPath("created_at").description("시간")
                                )
                        )
                );
    }

    @Test
    @DisplayName("결제 성공 시 응답")
    void t2() throws Exception {
        Amount amount = Amount.builder()
                .total(1000)
                .tax_free(0)
                .vat(0)
                .point(0)
                .discount(0)
                .green_deposit(0)
                .build();

        CardInfo cardInfo = CardInfo.builder()
                .interest_free_install("몰라")
                .bin("몰라")
                .card_type("카드 타입")
                .card_mid("몰라")
                .approved_id("승인 id?")
                .install_month("몰라")
                .purchase_corp("몰라")
                .purchase_corp_code("몰라")
                .issuer_corp("?")
                .issuer_corp_code("?")
                .kakaopay_purchase_corp("?")
                .kakaopay_purchase_corp_code("?")
                .kakaopay_issuer_corp("?")
                .kakaopay_issuer_corp_code("?")
                .build();

        ApprovalResponseDto approvalResponseDto = ApprovalResponseDto.builder()
                .cid("cid")
                .aid("aid")
                .tid("tid")
                .partner_user_id("user login id")
                .partner_order_id("?")
                .payment_method_type("결제 수단, CARD 또는 MONEY 중 하나")
                .amount(amount)
                .card_info(cardInfo)
                .item_name("point")
                .quantity(1)
                .created_at(LocalDateTime.now())
                .approved_at(LocalDateTime.now())
                .build();

        when(paymentService.approvePayment(anyString())).thenReturn(Mono.just(approvalResponseDto));

        MvcResult mvcResult = mockMvc.perform(get(baseUrl + "/success")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pg_token", "pgToken"))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andDo(
                        document("/payment/success",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    @DisplayName("포인트 송금")
    @WithMockUser(username = "testUser")
    void t3() throws Exception {
        SendRequestDto sendRequestDto = SendRequestDto.builder()
                .receiver("1234")
                .amount(1234)
                .build();

        when(paymentService.sendPoint(any(), anyString())).thenReturn(Mono.empty());

        mockMvc.perform(post(baseUrl + "/send")
                        .header("Authorization", "Bearer JWT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(sendRequestDto)))
                .andExpect(status().isOk())
                .andDo(
                        document("/payment/send",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("receiver").description("받는 사람"),
                                        fieldWithPath("amount").description("금액")
                                )
                        )
                );
    }
}
