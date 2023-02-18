package com.example.flightbookingsystemwithspringboot.resource;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonPropertyOrder({"identifier", "name", "description", "ticketPrice"})
public class FlightResponse implements Serializable {

    private static final long serialVersionUID = 1479384486514182298L;

    private String identifier;
    private String name;
    private String description;
    private BigDecimal ticketPrice;

    public FlightResponse(final String identifier, final String name, final String description, final BigDecimal ticketPrice) {
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.ticketPrice = ticketPrice;
    }

    public String toString() {
        return "FlightResponse{" +
                "identifier=" + identifier +
                ", name=" + name +
                ", description=" + description +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}
