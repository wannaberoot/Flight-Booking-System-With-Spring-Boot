package com.example.flightbookingsystemwithspringboot.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FlightListResource {

    @JsonProperty("flight")
    private FlightListResponse flightListResponse;

    public FlightListResource(final FlightListResponse flightListResponse) {
        this.flightListResponse = flightListResponse;
    }

    public String toString() {
        return "FlightListResource{" +
                "flightListResponse=" + flightListResponse +
                '}';
    }
}
