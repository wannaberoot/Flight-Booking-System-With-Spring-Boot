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
public class UpdateSeatDto implements Serializable {

    private static final long serialVersionUID = 218195292886179492L;

    @NotNull
    @NotEmpty
    private List<String> seatNumbers;
    @NotNull
    @NotEmpty
    private String seatStatus;

    public String toString() {
        return "UpdateSeatDto{" +
                "seatNumbers=" + seatNumbers +
                "seatStatus=" + seatStatus +
                '}';
    }
}
