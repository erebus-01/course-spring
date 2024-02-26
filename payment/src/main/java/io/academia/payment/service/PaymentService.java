package io.academia.payment.service;

import io.academia.payment.dto.request.CreatePaymentRequest;
import io.academia.payment.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(CreatePaymentRequest paymentRequest,
                                  String bank, String cardType,
                                  String orderInfo, String transactionStatus);
}
