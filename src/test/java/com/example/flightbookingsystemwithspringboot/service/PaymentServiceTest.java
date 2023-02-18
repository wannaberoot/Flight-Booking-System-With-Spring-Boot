package com.example.flightbookingsystemwithspringboot.service;

import com.example.flightbookingsystemwithspringboot.constants.SeatStatus;
import com.example.flightbookingsystemwithspringboot.dto.PaymentDto;
import com.example.flightbookingsystemwithspringboot.entity.Flight;
import com.example.flightbookingsystemwithspringboot.entity.Seat;
import com.example.flightbookingsystemwithspringboot.exception.ClientException;
import com.example.flightbookingsystemwithspringboot.exception.PaymentException;
import com.example.flightbookingsystemwithspringboot.exception.ResourceNotFoundException;
import com.example.flightbookingsystemwithspringboot.resource.PaymentResponse;
import com.example.flightbookingsystemwithspringboot.repository.PaymentRepository;
import com.iyzipay.model.Status;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {

    private static final String SEAT_NUMBER = "mockSeatNumber";
    private static final List<String> SEAT_NUMBERS = Arrays.asList(SEAT_NUMBER);
    private static final String CARD_HOLDER_NAME = "John Doe";
    private static final String CARD_NUMBER = "5451030000000000";
    private static final String INVALID_CARD_NUMBER = "4130111111111118";
    private static final String CARD_EXPIRE_MONTH = "12";
    private static final String CARD_EXPIRE_YEAR = "2030";
    private static final String CARD_CVC = "123";
    private static final BigDecimal TICKET_PRICE = BigDecimal.ONE;

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private SeatService seatService;
    @Mock
    private FlightService flightService;
    @InjectMocks
    private PaymentService underTest;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        underTest = new PaymentService(paymentRepository, seatService, flightService);
    }

    @Test
    public void create_payment_and_return_response() throws ResourceNotFoundException, PaymentException, ClientException {
        // Given
        Flight flight = new Flight();
        flight.setTicketPrice(TICKET_PRICE);
        when(flightService.getFlight(flight.getIdentifier())).thenReturn(flight);
        Seat seat = new Seat();
        seat.setSeatNumber(SEAT_NUMBER);
        seat.setSeatStatus(SeatStatus.AVAILABLE);
        seat.setFlight(flight);
        when(seatService.getSeat(flight, SEAT_NUMBER)).thenReturn(seat);
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setSeatNumbers(SEAT_NUMBERS);
        paymentDto.setCardHolderName(CARD_HOLDER_NAME);
        paymentDto.setCardNumber(CARD_NUMBER);
        paymentDto.setCardExpireMonth(CARD_EXPIRE_MONTH);
        paymentDto.setCardExpireYear(CARD_EXPIRE_YEAR);
        paymentDto.setCardCvc(CARD_CVC);
        com.iyzipay.model.Payment iyzipayPayment = new com.iyzipay.model.Payment();
        iyzipayPayment.setStatus(Status.SUCCESS.getValue());

        // When
        PaymentResponse paymentResponse = underTest.createPayment(flight.getIdentifier(), paymentDto);

        // Then
        Assertions.assertEquals(flight.getIdentifier(), paymentResponse.getFlightId());
        Assertions.assertEquals(SEAT_NUMBERS, paymentResponse.getSeatNumbers());
        Assertions.assertEquals("Payment successful!", paymentResponse.getPaymentStatus());
    }

    @Test(expected = PaymentException.class)
    public void create_payment_when_seat_has_already_been_sold_then_throw_exception() throws ResourceNotFoundException, PaymentException, ClientException {
        // Given
        Flight flight = new Flight();
        flight.setTicketPrice(TICKET_PRICE);
        when(flightService.getFlight(flight.getIdentifier())).thenReturn(flight);
        Seat seat = new Seat();
        seat.setSeatNumber(SEAT_NUMBER);
        seat.setSeatStatus(SeatStatus.SOLD);
        seat.setFlight(flight);
        when(seatService.getSeat(flight, SEAT_NUMBER)).thenReturn(seat);
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setSeatNumbers(SEAT_NUMBERS);
        paymentDto.setCardHolderName(CARD_HOLDER_NAME);
        paymentDto.setCardNumber(CARD_NUMBER);
        paymentDto.setCardExpireMonth(CARD_EXPIRE_MONTH);
        paymentDto.setCardExpireYear(CARD_EXPIRE_YEAR);
        paymentDto.setCardCvc(CARD_CVC);

        // When
        underTest.createPayment(flight.getIdentifier(), paymentDto);
    }

    @Test(expected = PaymentException.class)
    public void create_payment_when_card_information_are_invalid_then_throw_exception() throws ResourceNotFoundException, PaymentException, ClientException {
        // Given
        Flight flight = new Flight();
        flight.setTicketPrice(TICKET_PRICE);
        when(flightService.getFlight(flight.getIdentifier())).thenReturn(flight);
        Seat seat = new Seat();
        seat.setSeatNumber(SEAT_NUMBER);
        seat.setSeatStatus(SeatStatus.AVAILABLE);
        seat.setFlight(flight);
        when(seatService.getSeat(flight, SEAT_NUMBER)).thenReturn(seat);
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setSeatNumbers(SEAT_NUMBERS);
        paymentDto.setCardHolderName(CARD_HOLDER_NAME);
        paymentDto.setCardNumber(INVALID_CARD_NUMBER);
        paymentDto.setCardExpireMonth(CARD_EXPIRE_MONTH);
        paymentDto.setCardExpireYear(CARD_EXPIRE_YEAR);
        paymentDto.setCardCvc(CARD_CVC);
        com.iyzipay.model.Payment iyzipayPayment = new com.iyzipay.model.Payment();
        iyzipayPayment.setStatus(Status.FAILURE.getValue());

        // When
        underTest.createPayment(flight.getIdentifier(), paymentDto);
    }
}
