package com.example.flightbookingsystemwithspringboot.exception;

public class PaymentException extends Exception {

    private static final long serialVersionUID = 1678262113742045119L;

    private final String resource;
    private final String reasonCode;

    public PaymentException(final String resource, final String reasonCode, final String reasonText) {
        super(reasonText);
        this.resource = resource;
        this.reasonCode = reasonCode;
    }

    public String getResource() {
        return resource;
    }

    public String getReasonCode() {
        return reasonCode;
    }
}
