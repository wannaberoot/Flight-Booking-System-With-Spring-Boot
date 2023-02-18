package com.example.flightbookingsystemwithspringboot.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FlightResource {

    @JsonProperty("flight")
    private FlightResponse flightResponse;

    public FlightResource(final FlightResponse flightResponse) {
        this.flightResponse = flightResponse;
    }

    public String toString() {
        return "FlightResource{" +
                "flightResponse=" + flightResponse +
                '}';
    }
}
