package com.example.flightbookingsystemwithspringboot.entity;

import com.example.flightbookingsystemwithspringboot.constants.SeatStatus;
import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "seat")
@Getter
@Setter
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(name = "seat_number")
    private String seatNumber;
    @Column
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private SeatStatus seatStatus;
    @ManyToOne
    @JoinTable(name = "flight_seat", joinColumns = @JoinColumn(name = "seat_id"), inverseJoinColumns = @JoinColumn(name = "flight_id"))
    private Flight flight;

    public Seat(final String seatNumber, final SeatStatus seatStatus, final Flight flight) {
        this.seatNumber = seatNumber;
        this.seatStatus = seatStatus;
        this.flight = flight;
    }
}
