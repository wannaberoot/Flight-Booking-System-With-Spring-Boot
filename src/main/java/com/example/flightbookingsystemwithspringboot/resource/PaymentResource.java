package com.example.flightbookingsystemwithspringboot.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentResource {

    @JsonProperty("payment")
    private PaymentResponse paymentResponse;

    public PaymentResource(final PaymentResponse paymentResponse) {
        this.paymentResponse = paymentResponse;
    }

    public String toString() {
        return "PaymentResource{" +
                "paymentResponse=" + paymentResponse +
                '}';
    }
}
