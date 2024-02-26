package io.academia.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRes {
    private String status;
    private String message;
    private String URL;
}
