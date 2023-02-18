package com.example.flightbookingsystemwithspringboot.resource;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.example.flightbookingsystemwithspringboot.constants.SeatStatus;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
@JsonPropertyOrder({"flightId", "seatNumbers", "seatStatus"})
public class SeatResponse implements Serializable {

    private static final long serialVersionUID = 3319081698208469939L;

    private String flightId;
    private List<String> seatNumbers;
    private SeatStatus seatStatus;

    public SeatResponse(final String flightId, final List<String> seatNumbers, final SeatStatus seatStatus) {
        this.flightId = flightId;
        this.seatNumbers = seatNumbers;
        this.seatStatus = seatStatus;
    }

    public String toString() {
        return "SeatResponse{" +
                "flightId=" + flightId +
                ", seatNumbers=" + seatNumbers +
                ", seatStatus=" + seatStatus +
                '}';
    }
}
