package com.booking.member.payments.Service;

import com.booking.member.payments.dto.*;
import reactor.core.publisher.Mono;

public interface PaymentService {

    Mono<ReadyPaymentResponseDto> readyPayment(ReadyPaymentRequestDto req,String loginId);

    Mono<ApprovalResponseDto> approvePayment(String pgToken);

    Mono<Void> sendPoint(SendRequestDto req,String loginId);

    Mono<Void> resendPoint(ReSendRequestDto req);
}
