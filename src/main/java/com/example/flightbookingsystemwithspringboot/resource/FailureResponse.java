package com.example.flightbookingsystemwithspringboot.resource;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import java.io.Serializable;

@Data
@JsonPropertyOrder({"input", "reasonCode", "reasonText"})
public class FailureResponse implements Serializable {

    private static final long serialVersionUID = 3599968990685824439L;

    private String input;
    private String reasonCode;
    private String reasonText;

    public FailureResponse(final String input, final String reasonCode, final String reasonText) {
        this.input = input;
        this.reasonCode = reasonCode;
        this.reasonText = reasonText;
    }

    public String toString() {
        return "FailureResponse{" +
                "resource=" + input +
                ", reasonCode=" + reasonCode +
                ", reasonText=" + reasonText +
                '}';
    }
}
