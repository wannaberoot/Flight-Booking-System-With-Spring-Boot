package com.example.flightbookingsystemwithspringboot.controller;

import com.example.flightbookingsystemwithspringboot.constants.ApiPathValues;
import com.example.flightbookingsystemwithspringboot.exception.ResourceNotFoundException;
import com.example.flightbookingsystemwithspringboot.dto.FlightDto;
import com.example.flightbookingsystemwithspringboot.resource.FailureResource;
import com.example.flightbookingsystemwithspringboot.resource.FailureResponse;
import com.example.flightbookingsystemwithspringboot.resource.FlightResource;
import com.example.flightbookingsystemwithspringboot.resource.FlightResponse;
import com.example.flightbookingsystemwithspringboot.service.FlightService;
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
@Tag(name = "Flight", description = "API for flight operations.")
@RestController
@RequestMapping(ApiPathValues.REQUEST_BASE)
public class FlightController {

    private final FlightService flightService;

    @Autowired
    public FlightController(final FlightService flightService) {
        this.flightService =  flightService;
    }

    @Operation(summary = "Add Flight", description = "Add a flight.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added.",
                    content = @Content(schema = @Schema(implementation = FlightResponse.class)))})
    @PostMapping("/" + ApiPathValues.FLIGHT)
    public ResponseEntity<FlightResource> addFlight(@Valid @RequestBody final FlightDto flightDto) {
        FlightResponse flightResponse = flightService.addFlight(flightDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FlightResource(flightResponse));
    }

    @Operation(summary = "Find Flight", description = "Find a flight.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found.",
                    content = @Content(schema = @Schema(implementation = FlightResponse.class))),
            @ApiResponse(responseCode = "404", description = "Flight not found.",
                    content = @Content(schema = @Schema(implementation = FailureResponse.class)))})
    @GetMapping("/" + ApiPathValues.FLIGHT + "/" + "{flightId}")
    public ResponseEntity<?> queryFlight(
            @PathVariable final String flightId) {
        try {
            FlightResponse flightResponse = flightService.queryFlight(flightId);
            return ResponseEntity.status(HttpStatus.OK).body(new FlightResource(flightResponse));
        } catch (ResourceNotFoundException ex) {
            FailureResponse failureResponse = new FailureResponse(ex.getResource(), ex.getReasonCode(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailureResource(failureResponse));
        }
    }

    @Operation(summary = "Update Flight", description = "Update a flight.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully updated.",
                    content = @Content(schema = @Schema(implementation = FlightResponse.class))),
            @ApiResponse(responseCode = "404", description = "Flight not found.",
                    content = @Content(schema = @Schema(implementation = FailureResponse.class)))})
    @PatchMapping("/" + ApiPathValues.FLIGHT + "/" + "{flightId}")
    public ResponseEntity<?> updateFlight(
            @PathVariable final String flightId,
            @Valid @RequestBody final FlightDto flightDto) {
        try {
            FlightResponse flightResponse = flightService.updateFlight(flightId, flightDto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new FlightResource(flightResponse));
        } catch (ResourceNotFoundException ex) {
            FailureResponse failureResponse = new FailureResponse(ex.getResource(), ex.getReasonCode(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailureResource(failureResponse));
        }
    }

    @Operation(summary = "Remove Flight", description = "Remove a flight.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully removed."),
            @ApiResponse(responseCode = "404", description = "Flight not found.",
                    content = @Content(schema = @Schema(implementation = FailureResponse.class)))})
    @DeleteMapping("/" + ApiPathValues.FLIGHT + "/" + "{flightId}")
    public ResponseEntity<?> removeFlight(
            @PathVariable final String flightId) {
        try {
            flightService.removeFlight(flightId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (ResourceNotFoundException ex) {
            FailureResponse failureResponse = new FailureResponse(ex.getResource(), ex.getReasonCode(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailureResource(failureResponse));
        }
    }
}
