package com.example.flightbookingsystemwithspringboot.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FailureResource {

    @JsonProperty("error")
    private FailureResponse failureResponse;

    public FailureResource(final FailureResponse failureResponse) {
        this.failureResponse = failureResponse;
    }

    public String toString() {
        return "FailureResource{" +
                "flightFailureResponse=" + failureResponse +
                '}';
    }
}
