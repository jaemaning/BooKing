package com.booking.member.payments.domain;

import com.booking.member.members.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

//@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "payments")
@AllArgsConstructor
@Builder
public class Payment {
    @Id
//    @GeneratedValue
    @Column("payments_id")
    private Integer id;

    private String tid;

    private LocalDateTime approved_at;

    private Integer amount;

    private PaymentType type;

//    @ManyToOne
//    @JoinColumn(name = "payer")
//    private Member payer;
    @Column("payer")
    private Integer payer;

//    @ManyToOne
//    @JoinColumn(name = "receiver")
//    private Member receiver;
    @Column("receiver")
    private Integer receiver;

    public static Payment paymentTypeSend(Member sender, Member receiver,Integer amount) {
        return Payment.builder()
                .approved_at(LocalDateTime.now())
                .amount(amount)
                .type(PaymentType.Send)
                .payer(sender.getId())
                .receiver(receiver.getId())
                .build();
    }

    public static Payment paymentTypeReceive(Member sender, Member receiver,Integer amount) {
        return Payment.builder()
                .approved_at(LocalDateTime.now())
                .amount(amount)
                .type(PaymentType.Receive)
                .payer(sender.getId())
                .receiver(receiver.getId())
                .build();
    }
}
