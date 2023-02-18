package com.example.flightbookingsystemwithspringboot.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@JsonTypeName("flight")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@Data
public class FlightDto implements Serializable {

    private static final long serialVersionUID = 9169465768409372073L;

    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String description;
    @NotNull
    private BigDecimal ticketPrice;

    public String toString() {
        return "FlightDto{" +
                "name=" + name +
                ", description=" + description +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}
