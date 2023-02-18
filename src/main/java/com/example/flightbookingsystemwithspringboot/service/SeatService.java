package com.example.flightbookingsystemwithspringboot.service;

import com.example.flightbookingsystemwithspringboot.constants.SeatStatus;
import com.example.flightbookingsystemwithspringboot.dto.SeatDto;
import com.example.flightbookingsystemwithspringboot.dto.UpdateSeatDto;
import com.example.flightbookingsystemwithspringboot.entity.Seat;
import com.example.flightbookingsystemwithspringboot.exception.ClientException;
import com.example.flightbookingsystemwithspringboot.exception.ResourceNotFoundException;
import com.example.flightbookingsystemwithspringboot.resource.SeatResponse;
import com.example.flightbookingsystemwithspringboot.constants.DetailedErrors;
import com.example.flightbookingsystemwithspringboot.entity.Flight;
import com.example.flightbookingsystemwithspringboot.repository.SeatRepository;
import com.example.flightbookingsystemwithspringboot.resource.FlightListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeatService {

    private Logger logger = LoggerFactory.getLogger(SeatService.class);
    private final SeatRepository seatRepository;
    private final FlightService flightService;

    @Autowired
    public SeatService(final SeatRepository seatRepository, final FlightService flightService) {
        this.seatRepository = seatRepository;
        this.flightService = flightService;
    }

    @Transactional
    public SeatResponse addSeat(final String flightId, final SeatDto seatDto) throws ResourceNotFoundException {
        Flight flight = flightService.getFlight(flightId);
        List<String> seatNumbers = seatDto.getSeatNumbers();
        SeatStatus seatStatus = SeatStatus.AVAILABLE;
        for (String seatNumber : seatNumbers) {
            Seat seat = new Seat(seatNumber, SeatStatus.AVAILABLE, flight);
            seatRepository.save(seat);
            logger.info("Seat saved successfully!");
        }
        SeatResponse seatResponse = prepareSeatResource(flightId, seatNumbers, seatStatus);
        return seatResponse;
    }

    @Transactional
    public FlightListResponse queryAvailableSeats(final String flightId) throws ResourceNotFoundException {
        Flight flight = flightService.getFlight(flightId);
        List<Seat> availableSeats = getAvailableSeats(flight, SeatStatus.AVAILABLE);
        List<String> availableSeatNumbers = availableSeats.stream().map(availableSeat -> availableSeat.getSeatNumber()).collect(Collectors.toList());
        FlightListResponse flightListResponse = new FlightListResponse(flight.getIdentifier(), flight.getName(), flight.getDescription(), flight.getTicketPrice(), availableSeatNumbers);
        return flightListResponse;
    }

    @Transactional
    public SeatResponse updateSeat(final String flightId, final UpdateSeatDto updateSeatDto) throws ResourceNotFoundException, ClientException {
        Flight flight = flightService.getFlight(flightId);
        List<String> seatNumbers = updateSeatDto.getSeatNumbers();
        SeatStatus seatStatus = SeatStatus.getSeatStatusByValue(updateSeatDto.getSeatStatus());
        updateSeatStatus(flight, seatNumbers, seatStatus);
        logger.info("Seat updated successfully!");
        SeatResponse seatResponse = prepareSeatResource(flightId, seatNumbers, seatStatus);
        return seatResponse;
    }

    @Transactional
    public void removeSeat(final String flightId, final SeatDto seatDto) throws ResourceNotFoundException, ClientException {
        Flight flight = flightService.getFlight(flightId);
        List<String> seatNumbers = seatDto.getSeatNumbers();
        for (String seatNumber : seatNumbers) {
            Seat seat = getSeat(flight, seatNumber);
            if (SeatStatus.SOLD.equals(seat.getSeatStatus())) {
                throw new ClientException(seat.getSeatNumber(), DetailedErrors.SEAT_OPERATION_FAILED.getReasonCode(), DetailedErrors.SEAT_OPERATION_FAILED.getReasonText());
            }
            seatRepository.deleteSeatByFlightAndSeatNumber(flight, seat.getSeatNumber());
            logger.info("Seat removed successfully!");
        }
    }

    public Seat getSeat(final Flight flight, final String seatNumber) throws ResourceNotFoundException {
        Optional<Seat> seatOptional = seatRepository.findSeatByFlightAndSeatNumber(flight, seatNumber);
        if (seatOptional.isEmpty()) {
            logger.error("No seat found for seat number: " + seatNumber);
            throw new ResourceNotFoundException(flight.getIdentifier() + ", Seat: " + seatNumber, DetailedErrors.SEAT_NOT_FOUND.getReasonCode(), DetailedErrors.SEAT_NOT_FOUND.getReasonText());
        }
        return seatOptional.get();
    }

    public void updateSeatStatus(final Flight flight, final List<String> seatNumbers, final SeatStatus seatStatus) throws ResourceNotFoundException, ClientException {
        for (String seatNumber : seatNumbers) {
            Seat seat = getSeat(flight, seatNumber);
            if (SeatStatus.SOLD.equals(seat.getSeatStatus())) {
                logger.error("Seat can not be updated since it's Sold for seat number: " + seatNumber);
                throw new ClientException(seat.getSeatNumber(), DetailedErrors.SEAT_OPERATION_FAILED.getReasonCode(), DetailedErrors.SEAT_OPERATION_FAILED.getReasonText());
            }
            seat.setSeatStatus(seatStatus);
            seatRepository.save(seat);
            logger.info("Seat updated successfully!");
        }
    }

    private List<Seat> getAvailableSeats(final Flight flight, final SeatStatus seatStatus) throws ResourceNotFoundException {
        Optional<List<Seat>> availableSeatsOptional = seatRepository.findSeatsByFlightAndSeatStatus(flight, SeatStatus.AVAILABLE);
        if (availableSeatsOptional.get().isEmpty()) {
            logger.error("No available seat found for flight: " + flight.getIdentifier());
            throw new ResourceNotFoundException(flight.getIdentifier(), DetailedErrors.AVAILABLE_SEAT_NOT_FOUND.getReasonCode(), DetailedErrors.AVAILABLE_SEAT_NOT_FOUND.getReasonText());
        }
        return availableSeatsOptional.get();
    }

    private SeatResponse prepareSeatResource(final String flightId, final List<String> seatNumbers, final SeatStatus seatStatus) {
        final SeatResponse seatResponse = new SeatResponse(flightId, seatNumbers, seatStatus);
        return seatResponse;
    }
}
