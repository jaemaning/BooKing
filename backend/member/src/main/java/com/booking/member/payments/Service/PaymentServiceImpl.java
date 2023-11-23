package com.booking.member.payments.Service;

import com.booking.member.members.domain.Member;
import com.booking.member.members.repository.MemberRepository;
import com.booking.member.payments.Repository.PaymentRepository;
import com.booking.member.payments.domain.Payment;
import com.booking.member.payments.domain.PaymentType;
import com.booking.member.payments.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static com.booking.member.payments.domain.Payment.paymentTypeReceive;
import static com.booking.member.payments.domain.Payment.paymentTypeSend;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Value("${kakaopay.admin.key}")
    private String key;

    @Value("${kakaopay.approval.url}")
    private String approvalUrl;

    @Value("${kakaopay.fail.url}")
    private String failUrl;

    @Value("${kakaopay.cancle.url}")
    private String cancleUrl;

    private final WebClient webClient = WebClient.create("https://kapi.kakao.com");

    private final String cid="TC0ONETIME";

    private ReadyPaymentResponseDto readyPaymentResponseDto;

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;

    private String userLoginId;


    @Override
    public Mono<ReadyPaymentResponseDto> readyPayment(ReadyPaymentRequestDto req,String loginId) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("cid", cid);
        formData.add("partner_order_id", "POI");
        formData.add("partner_user_id", loginId);
        formData.add("item_name", "point");
        formData.add("quantity", "1");
        formData.add("total_amount", String.valueOf(req.amount()));
        formData.add("tax_free_amount", "0");
        formData.add("approval_url", approvalUrl);
        formData.add("cancel_url", cancleUrl);
        formData.add("fail_url", failUrl);

        return webClient.post()
                .uri("/v1/payment/ready")
                .header("Authorization", "KakaoAK " + key)
                .header("Content-Type","application/x-www-form-urlencoded")
//                .headers(headers -> headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                .bodyValue(formData)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class).doOnNext(body -> {
                        // 로그에 오류 본문을 출력
                        log.error("4xx error: {}", body);
                    }).then(Mono.error(new RuntimeException("Client error")));
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class).doOnNext(body -> {
                        // 로그에 오류 본문을 출력
                        log.error("5xx error: {}", body);
                    }).then(Mono.error(new RuntimeException("Server error")));
                })
                .bodyToMono(ReadyPaymentResponseDto.class)
                .doOnNext(responseDto -> {
                    this.readyPaymentResponseDto = responseDto;
                    userLoginId=loginId;
                });
    }

    @Override
    public Mono<ApprovalResponseDto> approvePayment(String pgToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("cid", cid);
        formData.add("tid", readyPaymentResponseDto.tid());
        formData.add("partner_order_id", "POI");
        formData.add("partner_user_id", userLoginId);
        formData.add("pg_token", pgToken);

        return webClient.post()
                .uri("/v1/payment/approve")
                .header("Authorization", "KakaoAK " + key)
                .header("Content-Type","application/x-www-form-urlencoded")
//                .headers(headers -> headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
                .bodyValue(formData)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class).doOnNext(body -> {
                        // 로그에 오류 본문을 출력
                        log.error("4xx error: {}", body);
                    }).then(Mono.error(new RuntimeException("Client error")));
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class).doOnNext(body -> {
                        // 로그에 오류 본문을 출력
                        log.error("5xx error: {}", body);
                    }).then(Mono.error(new RuntimeException("Server error")));
                })
                .bodyToMono(ApprovalResponseDto.class)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(response -> {
                    memberRepository.findByLoginId(userLoginId)
                            .flatMap(member -> {
                                Payment payment = Payment.builder()
                                        .tid(response.getTid())
                                        .approved_at(response.getApproved_at())
                                        .amount(response.getAmount().getTotal())
                                        .type(PaymentType.Charge)
                                        .payer(member.getId())
                                        .build();
                                return paymentRepository.save(payment)
                                        .flatMap(savedPayment -> {
                                            member.setPoint(member.getPoint() + response.getAmount().getTotal());
                                            return memberRepository.save(member);
                                        });
                            })
                            .subscribe();
                });
    }

    @Override
    public Mono<Void> sendPoint(SendRequestDto req,String loginId) {
        return memberRepository.findByLoginId(loginId)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("사용자를 찾을 수 없습니다.")))
                .flatMap(sender -> {
                    if (sender.getPoint() < req.amount()) {
                        log.error("포인트 부족");
                        return Mono.error(new RuntimeException("포인트가 부족합니다."));
                    }

                    return memberRepository.findByLoginId(req.receiver())
                            .switchIfEmpty(Mono.error(new UsernameNotFoundException("팔로우 대상을 찾을 수 없습니다.")))
                            .flatMap(receiver -> {
                                // 포인트 송금 로직
                                Payment paymentSend = paymentTypeSend(sender, receiver, req.amount());
                                Payment paymentReceive = paymentTypeReceive(sender, receiver, req.amount());

                                return Mono.when(
                                        paymentRepository.save(paymentSend),
                                        paymentRepository.save(paymentReceive),
                                        Mono.fromRunnable(() -> {
                                                    sender.setPoint(sender.getPoint() - req.amount());
                                                    receiver.setPoint(receiver.getPoint() + req.amount());
                                                })
                                                .then(memberRepository.save(sender))
                                                .then(memberRepository.save(receiver))
                                );
                            });
                })
                .onErrorResume(e -> {
                    log.error("포인트 송금 에러: {}", e.getMessage());
                    return Mono.error(e);
                });
    }

    @Override
    public Mono<Void> resendPoint(ReSendRequestDto req) {
        return Mono.zip(
                        memberRepository.findByLoginId("kakao_3144707067"),
                        memberRepository.findById(req.receiverMemberPk())
                                .switchIfEmpty(Mono.error(new UsernameNotFoundException("없는 사용자입니다.")))
                )
                .flatMap(tuple -> {
                    Member sender = tuple.getT1();
                    Member receiver = tuple.getT2();

                    Payment paymentSend = paymentTypeSend(sender, receiver, req.amount());
                    Payment paymentReceive = paymentTypeReceive(sender, receiver, req.amount());

                    return Mono.when(
                            paymentRepository.save(paymentSend),
                            paymentRepository.save(paymentReceive),
                            Mono.defer(() -> {
                                sender.setPoint(sender.getPoint() - req.amount());
                                receiver.setPoint(receiver.getPoint() + req.amount());
                                return Mono.when(
                                        memberRepository.save(sender),
                                        memberRepository.save(receiver)
                                );
                            })
                    );
                })
                .then()
                .onErrorResume(e->{
                    log.error("참가비 환급 에러: {}",e.getMessage());
                    return Mono.error(e);
                });
    }
}
