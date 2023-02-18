package com.example.flightbookingsystemwithspringboot.service;

import com.example.flightbookingsystemwithspringboot.exception.ResourceNotFoundException;
import com.example.flightbookingsystemwithspringboot.resource.FlightResponse;
import com.example.flightbookingsystemwithspringboot.constants.DetailedErrors;
import com.example.flightbookingsystemwithspringboot.dto.FlightDto;
import com.example.flightbookingsystemwithspringboot.entity.Flight;
import com.example.flightbookingsystemwithspringboot.repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class FlightService {

    private Logger logger = LoggerFactory.getLogger(FlightService.class);
    private final FlightRepository flightRepository;

    @Autowired
    public FlightService(final FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Transactional
    public FlightResponse addFlight(final FlightDto flightDto) {
        String flightName = flightDto.getName();
        String flightDescription = flightDto.getDescription();
        BigDecimal flightTicketPrice = flightDto.getTicketPrice();
        Flight flight = new Flight(flightName, flightDescription, flightTicketPrice);
        flightRepository.save(flight);
        logger.info("Flight saved successfully!");
        FlightResponse flightResponse = prepareFlightResource(flight.getIdentifier(), flight.getName(), flight.getDescription(), flight.getTicketPrice());
        return flightResponse;
    }

    @Transactional
    public FlightResponse queryFlight(final String flightId) throws ResourceNotFoundException {
        Flight flight = getFlight(flightId);
        FlightResponse flightResponse = prepareFlightResource(flight.getIdentifier(), flight.getName(), flight.getDescription(), flight.getTicketPrice());
        return flightResponse;
    }

    @Transactional
    public FlightResponse updateFlight(final String flightId, final FlightDto flightDto) throws ResourceNotFoundException {
        Flight flight = getFlight(flightId);
        String flightName = flightDto.getName();
        String flightDescription = flightDto.getDescription();
        BigDecimal flightTicketPrice = flightDto.getTicketPrice();
        flight.setName(flightName);
        flight.setDescription(flightDescription);
        flight.setTicketPrice(flightTicketPrice);
        flightRepository.save(flight);
        logger.info("Flight updated successfully!");
        FlightResponse flightResponse = prepareFlightResource(flight.getIdentifier(), flight.getName(), flight.getDescription(), flight.getTicketPrice());
        return flightResponse;
    }

    @Transactional
    public void removeFlight(final String flightId) throws ResourceNotFoundException {
        Flight flight = getFlight(flightId);
        flightRepository.deleteFlightByIdentifier(flight.getIdentifier());
        logger.info("Flight removed successfully!");
    }

    public Flight getFlight(final String flightId) throws ResourceNotFoundException {
        Optional<Flight> flightOptional = flightRepository.findFlightByIdentifier(flightId);
        if (flightOptional.isEmpty()) {
            logger.error("No flight found for flight identifier: " + flightId);
            throw new ResourceNotFoundException(flightId, DetailedErrors.FLIGHT_NOT_FOUND.getReasonCode(), DetailedErrors.FLIGHT_NOT_FOUND.getReasonText());
        }
        return flightOptional.get();
    }

    private FlightResponse prepareFlightResource(final String identifier, final String name, final String description, final BigDecimal ticketPrice) {
        final FlightResponse flightResponse = new FlightResponse(identifier, name, description, ticketPrice);
        return flightResponse;
    }
}
