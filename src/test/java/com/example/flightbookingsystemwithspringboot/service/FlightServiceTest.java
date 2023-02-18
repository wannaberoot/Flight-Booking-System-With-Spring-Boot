package com.example.flightbookingsystemwithspringboot.service;

import com.example.flightbookingsystemwithspringboot.exception.ResourceNotFoundException;
import com.example.flightbookingsystemwithspringboot.resource.FlightResponse;
import com.example.flightbookingsystemwithspringboot.dto.FlightDto;
import com.example.flightbookingsystemwithspringboot.entity.Flight;
import com.example.flightbookingsystemwithspringboot.repository.FlightRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.math.BigDecimal;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlightServiceTest {

    private static final String FLIGHT_NAME = "mockFlightName";
    private static final String FLIGHT_DESCRIPTION = "mockFlightDescription";
    private static final BigDecimal TICKET_PRICE = BigDecimal.ZERO;

    @Mock
    private FlightRepository flightRepository;
    @InjectMocks
    private FlightService underTest;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        underTest = new FlightService(flightRepository);
    }

    @Test
    public void add_flight_and_return_response() {
        // Given
        FlightDto flightDto = new FlightDto();
        flightDto.setName(FLIGHT_NAME);
        flightDto.setDescription(FLIGHT_DESCRIPTION);
        flightDto.setTicketPrice(TICKET_PRICE);

        // When
        FlightResponse flightResponse = underTest.addFlight(flightDto);

        // Then
        verify(flightRepository).save(any());
        Assertions.assertNotNull(flightResponse.getIdentifier());
        Assertions.assertEquals(FLIGHT_NAME, flightResponse.getName());
        Assertions.assertEquals(FLIGHT_DESCRIPTION, flightResponse.getDescription());
        Assertions.assertEquals(TICKET_PRICE, flightResponse.getTicketPrice());
    }

    @Test
    public void query_flight_when_flight_exists_then_return_response() throws ResourceNotFoundException {
        // Given
        Flight flight = new Flight();
        when(flightRepository.findFlightByIdentifier(any())).thenReturn(Optional.of(flight));

        // When
        FlightResponse flightResponse = underTest.queryFlight(flight.getIdentifier());

        // Then
        Assertions.assertEquals(flight.getIdentifier(), flightResponse.getIdentifier());
        Assertions.assertEquals(flight.getName(), flightResponse.getName());
        Assertions.assertEquals(flight.getDescription(), flightResponse.getDescription());
        Assertions.assertEquals(flight.getTicketPrice(), flightResponse.getTicketPrice());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void query_flight_when_flight_does_not_exist_then_throw_exception() throws ResourceNotFoundException {
        // Given
        when(flightRepository.findFlightByIdentifier(any())).thenReturn(Optional.empty());

        // When
        underTest.queryFlight(any());
    }

    @Test
    public void update_flight_when_flight_exists_then_return_response() throws ResourceNotFoundException {
        // Given
        Flight flight = new Flight();
        when(flightRepository.findFlightByIdentifier(any())).thenReturn(Optional.of(flight));
        FlightDto flightDto = new FlightDto();
        flightDto.setName(FLIGHT_NAME);
        flightDto.setDescription(FLIGHT_DESCRIPTION);
        flightDto.setTicketPrice(TICKET_PRICE);

        // When
        FlightResponse flightResponse = underTest.updateFlight(flight.getIdentifier(), flightDto);

        // Then
        verify(flightRepository).save(flight);
        Assertions.assertEquals(flight.getIdentifier(), flightResponse.getIdentifier());
        Assertions.assertEquals(FLIGHT_NAME, flightResponse.getName());
        Assertions.assertEquals(FLIGHT_DESCRIPTION, flightResponse.getDescription());
        Assertions.assertEquals(TICKET_PRICE, flightResponse.getTicketPrice());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void update_flight_when_flight_does_not_exist_then_throw_exception() throws ResourceNotFoundException {
        // Given
        when(flightRepository.findFlightByIdentifier(any())).thenReturn(Optional.empty());
        FlightDto flightDto = new FlightDto();

        // When
        underTest.updateFlight(any(), flightDto);
    }

    @Test
    public void remove_flight_when_flight_exists_then_return_void() throws ResourceNotFoundException {
        // Given
        Flight flight = new Flight();
        when(flightRepository.findFlightByIdentifier(any())).thenReturn(Optional.of(flight));

        // When
        underTest.removeFlight(flight.getIdentifier());

        // Then
        verify(flightRepository).deleteFlightByIdentifier(any());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void remove_flight_when_flight_does_not_exist_then_throw_exception() throws ResourceNotFoundException {
        // Given
        when(flightRepository.findFlightByIdentifier(any())).thenReturn(Optional.empty());

        // When
        underTest.removeFlight(any());
    }

    @Test
    public void get_flight_when_flight_exists_then_return_response() throws ResourceNotFoundException {
        // Given
        Flight expectedFlight = new Flight();
        when(flightRepository.findFlightByIdentifier(any())).thenReturn(Optional.of(expectedFlight));

        // When
        Flight flight = underTest.getFlight(expectedFlight.getIdentifier());

        // Then
        Assertions.assertEquals(expectedFlight, flight);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void get_flight_when_flight_does_not_exist_then_throw_exception() throws ResourceNotFoundException {
        // Given
        when(flightRepository.findFlightByIdentifier(any())).thenReturn(Optional.empty());

        // When
        underTest.getFlight(any());
    }
}
