package com.booking.member.payments.Repository;

import com.booking.member.payments.domain.Payment;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PaymentRepository extends R2dbcRepository<Payment,String> {
}
