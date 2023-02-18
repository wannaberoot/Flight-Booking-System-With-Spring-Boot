package com.example.flightbookingsystemwithspringboot.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DetailedErrors {

    INVALID_INPUT("IYZ0001", "The input can not be empty."),
    FLIGHT_NOT_FOUND("IYZ0002", "No flight found for given identifier."),
    AVAILABLE_SEAT_NOT_FOUND("IYZ0003", "No available seat found for given flight identifier."),
    SEAT_NOT_FOUND("IYZ0004", "No seat found for given flight identifier and seat number."),
    INVALID_SEAT_STATUS("IYZ0005", "No seat status found for given value."),
    SEAT_ALREADY_SOLD("IYZ0006", "The requested seat has already been sold."),
    PAYMENT_FAILED("IYZ0007", "Payment failed for given flight identifier."),
    SEAT_OPERATION_FAILED("IYZ0008", "A sold seat can not be changed/deleted.");

    private String reasonCode;
    private String reasonText;

    DetailedErrors(final String reasonCode, final String reasonText) {
        this.reasonCode = reasonCode;
        this.reasonText = reasonText;
    }

    @JsonValue
    public String getReasonCode() {
        return reasonCode;
    }

    @JsonValue
    public String getReasonText() {
        return reasonText;
    }
}
