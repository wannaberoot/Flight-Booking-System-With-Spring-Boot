package com.example.flightbookingsystemwithspringboot.controller;

import com.example.flightbookingsystemwithspringboot.constants.ApiPathValues;
import com.example.flightbookingsystemwithspringboot.dto.SeatDto;
import com.example.flightbookingsystemwithspringboot.dto.UpdateSeatDto;
import com.example.flightbookingsystemwithspringboot.exception.ClientException;
import com.example.flightbookingsystemwithspringboot.exception.ResourceNotFoundException;
import com.example.flightbookingsystemwithspringboot.resource.*;
import com.example.flightbookingsystemwithspringboot.service.SeatService;
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
@Tag(name = "Seat", description = "API for seat operations.")
@RestController
@RequestMapping(ApiPathValues.REQUEST_BASE)
public class SeatController {

    private final SeatService seatService;

    @Autowired
    public SeatController(final SeatService seatService) {
        this.seatService = seatService;
    }

    @Operation(summary = "Add Seat", description = "Add seat for a flight.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added.",
                    content = @Content(schema = @Schema(implementation = SeatResponse.class))),
            @ApiResponse(responseCode = "404", description = "Flight not found.",
                    content = @Content(schema = @Schema(implementation = FailureResponse.class)))})
    @PostMapping("/" + ApiPathValues.FLIGHT + "/"+ "{flightId}" + "/" + ApiPathValues.SEAT)
    public ResponseEntity<?> addSeat(
            @PathVariable final String flightId,
            @Valid @RequestBody final SeatDto seatDto) {
        try {
            SeatResponse seatResponse = seatService.addSeat(flightId, seatDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new SeatResource(seatResponse));
        } catch (ResourceNotFoundException ex) {
            FailureResponse failureResponse = new FailureResponse(ex.getResource(), ex.getReasonCode(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailureResource(failureResponse));
        }
    }

    @Operation(summary = "List Available Seats", description = "List available seats for a flight.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully listed.",
                    content = @Content(schema = @Schema(implementation = FlightListResponse.class))),
            @ApiResponse(responseCode = "404", description = "Flight not found.",
                    content = @Content(schema = @Schema(implementation = FailureResponse.class)))})
    @GetMapping("/" + ApiPathValues.FLIGHT + "/"+ "{flightId}" + "/" + ApiPathValues.SEAT)
    public ResponseEntity<?> queryAvailableSeats(
            @PathVariable final String flightId) {
        try {
            FlightListResponse flightListResponse = seatService.queryAvailableSeats(flightId);
            return ResponseEntity.status(HttpStatus.OK).body(new FlightListResource(flightListResponse));
        } catch (ResourceNotFoundException ex) {
            FailureResponse failureResponse = new FailureResponse(ex.getResource(), ex.getReasonCode(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailureResource(failureResponse));
        }
    }

    @Operation(summary = "Update Seat", description = "Update seat for a flight.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully updated.",
                    content = @Content(schema = @Schema(implementation = SeatResponse.class))),
            @ApiResponse(responseCode = "400", description = "Update operation failed.",
                    content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Flight not found.",
                    content = @Content(schema = @Schema(implementation = FailureResponse.class)))})
    @PatchMapping("/" + ApiPathValues.FLIGHT + "/"+ "{flightId}" + "/" + ApiPathValues.SEAT)
    public ResponseEntity<?> updateSeat(
            @PathVariable final String flightId,
            @Valid @RequestBody final UpdateSeatDto updateSeatDto) {
        try {
            SeatResponse seatResponse = seatService.updateSeat(flightId, updateSeatDto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SeatResource(seatResponse));
        } catch (ClientException ex) {
            FailureResponse failureResponse = new FailureResponse(ex.getResource(), ex.getReasonCode(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FailureResource(failureResponse));
        } catch (ResourceNotFoundException ex) {
            FailureResponse failureResponse = new FailureResponse(ex.getResource(), ex.getReasonCode(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailureResource(failureResponse));
        }
    }

    @Operation(summary = "Remove Seat", description = "Remove seat for a flight.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully removed."),
            @ApiResponse(responseCode = "400", description = "Delete operation failed.",
                    content = @Content(schema = @Schema(implementation = FailureResponse.class))),
            @ApiResponse(responseCode = "404", description = "Flight not found.",
                    content = @Content(schema = @Schema(implementation = FailureResponse.class)))})
    @DeleteMapping("/" + ApiPathValues.FLIGHT + "/"+ "{flightId}" + "/" + ApiPathValues.SEAT)
    public ResponseEntity<?> removeSeat(
            @PathVariable final String flightId,
            @Valid @RequestBody final SeatDto seatDto) {
        try {
            seatService.removeSeat(flightId, seatDto);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (ClientException ex) {
            FailureResponse failureResponse = new FailureResponse(ex.getResource(), ex.getReasonCode(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FailureResource(failureResponse));
        } catch (ResourceNotFoundException ex) {
            FailureResponse failureResponse = new FailureResponse(ex.getResource(), ex.getReasonCode(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailureResource(failureResponse));
        }
    }
}
