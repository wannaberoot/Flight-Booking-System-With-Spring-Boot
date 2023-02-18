package com.example.flightbookingsystemwithspringboot.constants;

import com.example.flightbookingsystemwithspringboot.exception.ResourceNotFoundException;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SeatStatus {

    AVAILABLE("Available"),
    SOLD("Sold");

    private String value;

    SeatStatus(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static SeatStatus getSeatStatusByValue(final String seatStatusToCheck) throws ResourceNotFoundException {
        for (SeatStatus seatStatus : SeatStatus.values()) {
            if (seatStatus.getValue().equalsIgnoreCase(seatStatusToCheck)) {
                return seatStatus;
            }
        }
        throw new ResourceNotFoundException(seatStatusToCheck, DetailedErrors.INVALID_SEAT_STATUS.getReasonCode(), DetailedErrors.INVALID_SEAT_STATUS.getReasonText());
    }
}
