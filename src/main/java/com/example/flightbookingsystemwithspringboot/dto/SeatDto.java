package com.example.flightbookingsystemwithspringboot.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@JsonTypeName("seat")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@Data
public class SeatDto implements Serializable {

    private static final long serialVersionUID = 185544333369659675L;

    @NotNull
    @NotEmpty
    private List<String> seatNumbers;

    public String toString() {
        return "SeatDto{" +
                "seatNumbers=" + seatNumbers +
                '}';
    }
}
