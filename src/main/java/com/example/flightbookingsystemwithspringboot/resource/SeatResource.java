package com.example.flightbookingsystemwithspringboot.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SeatResource {

    @JsonProperty("seat")
    private SeatResponse seatResponse;

    public SeatResource(final SeatResponse seatResponse) {
        this.seatResponse = seatResponse;
    }

    public String toString() {
        return "SeatResource{" +
                "seatResponse=" + seatResponse +
                '}';
    }
}
