package com.example.flightbookingsystemwithspringboot.controller;

import com.example.flightbookingsystemwithspringboot.exception.ResourceNotFoundException;
import com.example.flightbookingsystemwithspringboot.constants.ApiPathValues;
import com.example.flightbookingsystemwithspringboot.dto.PaymentDto;
import com.example.flightbookingsystemwithspringboot.exception.ClientException;
import com.example.flightbookingsystemwithspringboot.exception.PaymentException;
import com.example.flightbookingsystemwithspringboot.resource.FailureResource;
import com.example.flightbookingsystemwithspringboot.resource.FailureResponse;
import com.example.flightbookingsystemwithspringboot.resource.PaymentResource;
import com.example.flightbookingsystemwithspringboot.resource.PaymentResponse;
import com.example.flightbookingsystemwithspringboot.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Validated
@Tag(name = "Payment", description = "API for payment operations.")
@RestController
@RequestMapping(ApiPathValues.REQUEST_BASE)
public class PaymentController {

    private final PaymentService iyzicoPaymentService;

    @Autowired
    public PaymentController(final PaymentService iyzicoPaymentService) {
        this.iyzicoPaymentService = iyzicoPaymentService;
    }

    @Operation(summary = "Payment", description = "Make payment for flight ticket.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successful payment.",
                    content = @Content(schema = @Schema(implementation = PaymentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Payment failed because of bad request.",
                    content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Flight not found.",
                    content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "500", description = "Payment failed.",
                    content = @Content(schema = @Schema(implementation = FailureResponse.class)))})
    @PostMapping("/" + ApiPathValues.FLIGHT + "/"+ "{flightId}" + "/" + ApiPathValues.PAYMENT)
    public ResponseEntity<?> createPayment(
            @PathVariable final String flightId,
            @Valid @RequestBody PaymentDto paymentDto) {
        try {
            PaymentResponse paymentResponse = iyzicoPaymentService.createPayment(flightId, paymentDto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new PaymentResource(paymentResponse));
        } catch (ClientException ex) {
            FailureResponse failureResponse = new FailureResponse(ex.getResource(), ex.getReasonCode(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FailureResource(failureResponse));
        } catch (ResourceNotFoundException ex) {
            FailureResponse failureResponse = new FailureResponse(ex.getResource(), ex.getReasonCode(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailureResource(failureResponse));
        } catch (PaymentException ex) {
            FailureResponse failureResponse = new FailureResponse(ex.getResource(), ex.getReasonCode(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new FailureResource(failureResponse));
        }
    }
}
