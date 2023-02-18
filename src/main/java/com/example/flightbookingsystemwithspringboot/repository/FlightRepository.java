package com.example.flightbookingsystemwithspringboot.repository;

import com.example.flightbookingsystemwithspringboot.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findFlightByIdentifier(String identifier);
    void deleteFlightByIdentifier(String identifier);
}
