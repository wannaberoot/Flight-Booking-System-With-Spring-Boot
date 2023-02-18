package com.example.flightbookingsystemwithspringboot.service;

import com.example.flightbookingsystemwithspringboot.constants.SeatStatus;
import com.example.flightbookingsystemwithspringboot.dto.SeatDto;
import com.example.flightbookingsystemwithspringboot.dto.UpdateSeatDto;
import com.example.flightbookingsystemwithspringboot.entity.Flight;
import com.example.flightbookingsystemwithspringboot.entity.Seat;
import com.example.flightbookingsystemwithspringboot.exception.ClientException;
import com.example.flightbookingsystemwithspringboot.exception.ResourceNotFoundException;
import com.example.flightbookingsystemwithspringboot.repository.SeatRepository;
import com.example.flightbookingsystemwithspringboot.resource.FlightListResponse;
import com.example.flightbookingsystemwithspringboot.resource.SeatResponse;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SeatServiceTest {

    private static final String SEAT_NUMBER = "mockSeatNumber";
    private static final List<String> SEAT_NUMBERS = Arrays.asList(SEAT_NUMBER);

    @Mock
    private SeatRepository seatRepository;
    @Mock
    private FlightService flightService;
    @InjectMocks
    private SeatService underTest;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        underTest = new SeatService(seatRepository, flightService);
    }

    @Test
    public void add_seat_and_return_response() throws ResourceNotFoundException {
        // Given
        Flight flight = new Flight();
        when(flightService.getFlight(flight.getIdentifier())).thenReturn(flight);
        SeatDto seatDto = new SeatDto();
        seatDto.setSeatNumbers(SEAT_NUMBERS);

        // When
        SeatResponse seatResponse = underTest.addSeat(flight.getIdentifier(), seatDto);

        // Then
        verify(seatRepository).save(any());
        Assertions.assertEquals(flight.getIdentifier(), seatResponse.getFlightId());
        Assertions.assertEquals(SEAT_NUMBERS, seatResponse.getSeatNumbers());
        Assertions.assertEquals(SeatStatus.AVAILABLE, seatResponse.getSeatStatus());
    }

    @Test
    public void query_available_seats_when_exists_then_return_response() throws ResourceNotFoundException {
        // Given
        Flight flight = new Flight();
        Seat seat = new Seat(SEAT_NUMBER, SeatStatus.AVAILABLE, flight);
        List<Seat> seats = Arrays.asList(seat);
        when(flightService.getFlight(flight.getIdentifier())).thenReturn(flight);
        when(seatRepository.findSeatsByFlightAndSeatStatus(flight, SeatStatus.AVAILABLE)).thenReturn(Optional.of(seats));

        // When
        FlightListResponse response = underTest.queryAvailableSeats(flight.getIdentifier());

        // Then
        Assertions.assertEquals(flight.getIdentifier(), response.getIdentifier());
        Assertions.assertTrue(response.getAvailableSeatNumbers().contains(SEAT_NUMBER));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void query_available_seats_when_does_not_exist_then_throw_exception() throws ResourceNotFoundException {
        // Given
        Flight flight = new Flight();
        List<Seat> seats = new ArrayList<>();
        when(flightService.getFlight(flight.getIdentifier())).thenReturn(flight);
        when(seatRepository.findSeatsByFlightAndSeatStatus(flight, SeatStatus.AVAILABLE)).thenReturn(Optional.of(seats));

        // When
        underTest.queryAvailableSeats(flight.getIdentifier());
    }

    @Test
    public void update_seat_when_exists_then_return_response() throws ResourceNotFoundException, ClientException {
        // Given
        Flight flight = new Flight();
        Seat seat = new Seat(SEAT_NUMBER, SeatStatus.AVAILABLE, flight);
        when(flightService.getFlight(flight.getIdentifier())).thenReturn(flight);
        when(seatRepository.findSeatByFlightAndSeatNumber(flight, SEAT_NUMBER)).thenReturn(Optional.of(seat));
        UpdateSeatDto updateSeatDto = new UpdateSeatDto();
        updateSeatDto.setSeatNumbers(SEAT_NUMBERS);
        updateSeatDto.setSeatStatus(SeatStatus.SOLD.getValue());

        // When
        SeatResponse seatResponse = underTest.updateSeat(flight.getIdentifier(), updateSeatDto);

        // Then
        verify(seatRepository).save(any());
        Assertions.assertEquals(flight.getIdentifier(), seatResponse.getFlightId());
        Assertions.assertEquals(SEAT_NUMBERS, seatResponse.getSeatNumbers());
        Assertions.assertEquals(SeatStatus.SOLD, seatResponse.getSeatStatus());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void update_seat_when_does_not_exist_then_throw_exception() throws ResourceNotFoundException, ClientException {
        // Given
        Flight flight = new Flight();
        when(flightService.getFlight(flight.getIdentifier())).thenReturn(flight);
        UpdateSeatDto updateSeatDto = new UpdateSeatDto();

        // When
        underTest.updateSeat(flight.getIdentifier(), updateSeatDto);
    }

    @Test(expected = ClientException.class)
    public void update_seat_when_seat_has_already_been_sold_then_throw_exception() throws ResourceNotFoundException, ClientException {
        // Given
        Flight flight = new Flight();
        Seat seat = new Seat(SEAT_NUMBER, SeatStatus.SOLD, flight);
        when(flightService.getFlight(flight.getIdentifier())).thenReturn(flight);
        when(seatRepository.findSeatByFlightAndSeatNumber(flight, SEAT_NUMBER)).thenReturn(Optional.of(seat));
        UpdateSeatDto updateSeatDto = new UpdateSeatDto();
        updateSeatDto.setSeatNumbers(SEAT_NUMBERS);
        updateSeatDto.setSeatStatus(SeatStatus.AVAILABLE.getValue());

        // When
        underTest.updateSeat(flight.getIdentifier(), updateSeatDto);
    }

    @Test
    public void remove_seat_when_exists_then_return_void() throws ResourceNotFoundException, ClientException {
        // Given
        SeatDto seatDto = new SeatDto();
        seatDto.setSeatNumbers(SEAT_NUMBERS);
        Flight flight = new Flight();
        Seat seat = new Seat();
        seat.setSeatNumber(SEAT_NUMBER);
        seat.setSeatStatus(SeatStatus.AVAILABLE);
        seat.setFlight(flight);
        when(flightService.getFlight(flight.getIdentifier())).thenReturn(flight);
        when(seatRepository.findSeatByFlightAndSeatNumber(flight, SEAT_NUMBER)).thenReturn(Optional.of(seat));

        // When
        underTest.removeSeat(flight.getIdentifier(), seatDto);

        // Then
        verify(seatRepository).deleteSeatByFlightAndSeatNumber(any(),any());
    }

    @Test(expected = ClientException.class)
    public void remove_seat_when_seat_has_already_been_sold_then_throw_exception() throws ResourceNotFoundException, ClientException {
        // Given
        SeatDto seatDto = new SeatDto();
        seatDto.setSeatNumbers(SEAT_NUMBERS);
        Flight flight = new Flight();
        Seat seat = new Seat();
        seat.setSeatNumber(SEAT_NUMBER);
        seat.setSeatStatus(SeatStatus.SOLD);
        seat.setFlight(flight);
        when(flightService.getFlight(flight.getIdentifier())).thenReturn(flight);
        when(seatRepository.findSeatByFlightAndSeatNumber(flight, SEAT_NUMBER)).thenReturn(Optional.of(seat));

        // When
        underTest.removeSeat(flight.getIdentifier(), seatDto);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void get_flight_when_flight_does_not_exist_then_throw_exception() throws ResourceNotFoundException {
        // Given
        Optional<Seat> seat = Optional.empty();
        Flight flight = new Flight();
        when(seatRepository.findSeatByFlightAndSeatNumber(flight, SEAT_NUMBER)).thenReturn(seat);

        // When
        underTest.getSeat(flight, SEAT_NUMBER);
    }
}
