package com.example.flightbookingsystemwithspringboot.resource;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@JsonPropertyOrder({"identifier", "name", "description", "ticketPrice", "availableSeatNumbers"})
public class FlightListResponse implements Serializable {

    private static final long serialVersionUID = 3742604173143985473L;

    private String identifier;
    private String name;
    private String description;
    private BigDecimal ticketPrice;
    private List<String> availableSeatNumbers;

    public FlightListResponse(final String identifier, final String name, final String description, final BigDecimal ticketPrice, final List<String> availableSeatNumbers) {
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.ticketPrice = ticketPrice;
        this.availableSeatNumbers = availableSeatNumbers;
    }

    public String toString() {
        return "FlightListResponse{" +
                "identifier=" + identifier +
                ", name=" + name +
                ", description=" + description +
                ", ticketPrice=" + ticketPrice +
                ", availableSeatNumbers=" + availableSeatNumbers +
                '}';
    }
}
