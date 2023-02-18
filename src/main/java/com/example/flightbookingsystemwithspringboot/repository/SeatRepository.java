package com.example.flightbookingsystemwithspringboot.repository;

import com.example.flightbookingsystemwithspringboot.constants.SeatStatus;
import com.example.flightbookingsystemwithspringboot.entity.Flight;
import com.example.flightbookingsystemwithspringboot.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Lock(value = LockModeType.PESSIMISTIC_READ)
    Optional<Seat> findSeatByFlightAndSeatNumber(Flight flight, String seatNumber);
    Optional<List<Seat>> findSeatsByFlightAndSeatStatus(Flight flight, SeatStatus seatStatus);
    void deleteSeatByFlightAndSeatNumber(Flight flight, String seatNumber);
}
