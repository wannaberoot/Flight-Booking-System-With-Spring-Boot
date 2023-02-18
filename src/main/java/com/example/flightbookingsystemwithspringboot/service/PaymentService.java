package com.example.flightbookingsystemwithspringboot.service;

import com.example.flightbookingsystemwithspringboot.constants.IyzıcoApiValues;
import com.example.flightbookingsystemwithspringboot.constants.SeatStatus;
import com.example.flightbookingsystemwithspringboot.dto.PaymentDto;
import com.example.flightbookingsystemwithspringboot.entity.Flight;
import com.example.flightbookingsystemwithspringboot.entity.Seat;
import com.example.flightbookingsystemwithspringboot.exception.ClientException;
import com.example.flightbookingsystemwithspringboot.exception.PaymentException;
import com.example.flightbookingsystemwithspringboot.exception.ResourceNotFoundException;
import com.example.flightbookingsystemwithspringboot.resource.PaymentResponse;
import com.example.flightbookingsystemwithspringboot.constants.DetailedErrors;
import com.example.flightbookingsystemwithspringboot.entity.Payment;
import com.example.flightbookingsystemwithspringboot.repository.PaymentRepository;
import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreatePaymentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final SeatService seatService;
    private final FlightService flightService;

    @Autowired
    public PaymentService(final PaymentRepository paymentRepository, final SeatService seatService, final FlightService flightService) {
        this.paymentRepository = paymentRepository;
        this.seatService = seatService;
        this.flightService = flightService;
    }

    @Transactional(rollbackFor = PaymentException.class)
    public PaymentResponse createPayment(final String flightId, final PaymentDto paymentDto) throws ResourceNotFoundException, PaymentException, ClientException {
        Flight flight = flightService.getFlight(flightId);
        List<String> seatNumbers = new ArrayList<>();
        for (String seatNumber : paymentDto.getSeatNumbers()) {
            try {
                Seat seat = seatService.getSeat(flight, seatNumber);
                if (!SeatStatus.AVAILABLE.equals(seat.getSeatStatus())) {
                    throw new PaymentException(seat.getSeatNumber(), DetailedErrors.SEAT_ALREADY_SOLD.getReasonCode(), DetailedErrors.SEAT_ALREADY_SOLD.getReasonText());
                }
                seatNumbers.add(seat.getSeatNumber());
            } catch (Exception ex) {
                logger.error("Seat already sold for seat number: " + seatNumber);
                throw new PaymentException(seatNumber, DetailedErrors.SEAT_ALREADY_SOLD.getReasonCode(), DetailedErrors.SEAT_ALREADY_SOLD.getReasonText());
            }
        }
        BigDecimal price = calculatePrice(flight.getTicketPrice(), seatNumbers);
        com.iyzipay.model.Payment payment = createPaymentRequest(price, flight, paymentDto);
        if (Status.SUCCESS.getValue().equals(payment.getStatus())) {
            seatService.updateSeatStatus(flight, seatNumbers, SeatStatus.SOLD);
            savePayment(flight.getTicketPrice(), flightId, seatNumbers);
            logger.info("Payment successful for flight: " + flight.getIdentifier());
        } else {
            logger.error("Payment failed for flight: " + flight.getIdentifier());
            throw new PaymentException(flight.getIdentifier(), DetailedErrors.PAYMENT_FAILED.getReasonCode(), DetailedErrors.PAYMENT_FAILED.getReasonText());
        }
        PaymentResponse paymentResponse = preparePaymentResource(flightId, seatNumbers);
        return paymentResponse;
    }

    private void savePayment(final BigDecimal price, final String flightId, final List<String> seatNumbers) {
        for (String seatNo : seatNumbers) {
            Payment payment = new Payment();
            payment.setPrice(price);
            payment.setFlightId(flightId);
            payment.setSeatNo(seatNo);
            paymentRepository.save(payment);
            logger.info("Payment saved successfully!");
        }
    }

    private com.iyzipay.model.Payment createPaymentRequest(final BigDecimal price, final Flight flight, final PaymentDto paymentDto) {
        Options options = new Options();
        options.setApiKey(IyzıcoApiValues.API_KEY);
        options.setSecretKey(IyzıcoApiValues.SECRET_KEY);
        options.setBaseUrl(IyzıcoApiValues.BASE_URL);

        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setLocale(Locale.TR.getValue());
        request.setConversationId(UUID.randomUUID().toString());
        request.setPrice(price);
        request.setPaidPrice(price);
        request.setCurrency(Currency.TRY.name());
        request.setInstallment(1);
        request.setBasketId("B67832");
        request.setPaymentChannel(PaymentChannel.WEB.name());
        request.setPaymentGroup(PaymentGroup.PRODUCT.name());

        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardHolderName(paymentDto.getCardHolderName());
        paymentCard.setCardNumber(paymentDto.getCardNumber());
        paymentCard.setExpireMonth(paymentDto.getCardExpireMonth());
        paymentCard.setExpireYear(paymentDto.getCardExpireYear());
        paymentCard.setCvc(paymentDto.getCardCvc());
        paymentCard.setRegisterCard(0);
        request.setPaymentCard(paymentCard);

        Buyer buyer = new Buyer();
        buyer.setId("BY789");
        buyer.setName("John");
        buyer.setSurname("Doe");
        buyer.setGsmNumber("+905350000000");
        buyer.setEmail("email@email.com");
        buyer.setIdentityNumber("74300864791");
        buyer.setLastLoginDate("2015-10-05 12:43:35");
        buyer.setRegistrationDate("2013-04-21 15:12:09");
        buyer.setRegistrationAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        buyer.setIp("85.34.78.112");
        buyer.setCity("Istanbul");
        buyer.setCountry("Turkey");
        buyer.setZipCode("34732");
        request.setBuyer(buyer);

        Address shippingAddress = new Address();
        shippingAddress.setContactName("Jane Doe");
        shippingAddress.setCity("Istanbul");
        shippingAddress.setCountry("Turkey");
        shippingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        shippingAddress.setZipCode("34742");
        request.setShippingAddress(shippingAddress);

        Address billingAddress = new Address();
        billingAddress.setContactName("Jane Doe");
        billingAddress.setCity("Istanbul");
        billingAddress.setCountry("Turkey");
        billingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        billingAddress.setZipCode("34742");
        request.setBillingAddress(billingAddress);

        List<BasketItem> basketItems = new ArrayList<>();
        for (String seatNumber : paymentDto.getSeatNumbers()) {
            BasketItem basketItem = new BasketItem();
            basketItem.setId(flight.getName() + "-Seat" + seatNumber);
            basketItem.setName("Flight Ticket");
            basketItem.setCategory1("Travel");
            basketItem.setItemType(BasketItemType.VIRTUAL.name());
            basketItem.setPrice(flight.getTicketPrice());
            basketItems.add(basketItem);
        }
        request.setBasketItems(basketItems);

        com.iyzipay.model.Payment payment = com.iyzipay.model.Payment.create(request, options);
        System.out.println(payment);
        return payment;
    }

    private BigDecimal calculatePrice(final BigDecimal ticketPrice, final List<String> seatNumbers) {
        BigDecimal totalCost = ticketPrice.multiply(BigDecimal.valueOf(seatNumbers.size()));
        return totalCost;
    }

    private PaymentResponse preparePaymentResource(final String flightId, final List<String> seatNumbers) {
        final PaymentResponse paymentResponse = new PaymentResponse(flightId, seatNumbers, "Payment successful!");
        return paymentResponse;
    }
}
