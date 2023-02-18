package com.example.flightbookingsystemwithspringboot.resource;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
@JsonPropertyOrder({"flightId", "seatNumbers", "bankPaymentResult"})
public class PaymentResponse implements Serializable {

    private static final long serialVersionUID = -8007346185017914954L;

    private String flightId;
    private List<String> seatNumbers;
    private String paymentStatus;

    public PaymentResponse(final String flightId, final List<String> seatNumbers, final String paymentStatus) {
        this.flightId = flightId;
        this.seatNumbers = seatNumbers;
        this.paymentStatus = paymentStatus;
    }

    public String toString() {
        return "PaymentResponse{" +
                "flightId=" + flightId +
                ", seatNumbers=" + seatNumbers +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}
