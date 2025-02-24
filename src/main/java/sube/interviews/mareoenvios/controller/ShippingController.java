package sube.interviews.mareoenvios.controller;


import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sube.interviews.mareoenvios.entity.ShippingRequestDTO;
import sube.interviews.mareoenvios.entity.ShippingResponse;
import sube.interviews.mareoenvios.entity.State;
import sube.interviews.mareoenvios.exception.BadRequestException;
import sube.interviews.mareoenvios.exception.ResourceNotFoundException;
import sube.interviews.mareoenvios.service.ShippingService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    ShippingService shippingService;

    @ApiResponse(description = "Retrieves a Shipping")
    @GetMapping(value = "info/{shippingId}")
    public ShippingResponse getShipping(@PathVariable Long shippingId) {
        ShippingResponse shippingResponse = shippingService.getShipping(shippingId);
        if (shippingResponse == null || shippingResponse.getShipping() == null) {
            throw new ResourceNotFoundException("Shipping with ID " + shippingId + " not found.");
        }
        return shippingResponse;
    }


    @ApiResponse(description = "Shippings between dates")
    @GetMapping(value = "info/{sendDateFrom}/{sendDateTo}")
    public ResponseEntity getShipping(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sendDateFrom,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sendDateTo) {

        List<ShippingResponse> shippingResponses = shippingService.getShippingsBetweenDates(sendDateFrom, sendDateTo);

        if (shippingResponses.isEmpty()) {
            throw new ResourceNotFoundException("No shipments found between " + sendDateFrom + " and " + sendDateTo);
        }

        return ResponseEntity.ok(shippingResponses);
    }


    @ApiResponse(description = "Retrieves a Shipping for state")
    @GetMapping(value = "info/state/{state}")
    public ResponseEntity getShippingState(@PathVariable State state) {

        List<ShippingResponse> shippingResponses = shippingService.getShippingsByStates(state);

        if (shippingResponses.isEmpty()) {
            throw new ResourceNotFoundException("No shipments found with state: " + state);
        }

        return ResponseEntity.ok(shippingResponses);
    }


    @ApiResponse(description = "Change Shipping state to send to mail")
    @GetMapping(value = "transition/sendToMail/{shippingId}")
    public ResponseEntity<String> changeStateToSendToMail(@PathVariable Long shippingId) {
        try {
            shippingService.changeState(shippingId, State.ENTREGADO_AL_CORREO);
            return ResponseEntity.ok("State updated successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiResponse(description = "Change Shipping state to in travel")
    @GetMapping(value = "transition/inTravel/{shippingId}")
    public ResponseEntity<String> changeStateToInTravel(@PathVariable Long shippingId) {
        try {
            shippingService.changeState(shippingId, State.EN_CAMINO);
            return ResponseEntity.ok("State updated successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiResponse(description = "Change Shipping state to delivered")
    @GetMapping(value = "transition/delivered/{shippingId}")
    public ResponseEntity<String> changeStateToDelivered(@PathVariable Long shippingId) {
        try {
            shippingService.changeState(shippingId, State.ENTREGADO);
            return ResponseEntity.ok("State updated successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiResponse(description = "Change Shipping state to cancelled")
    @GetMapping(value = "transition/cancelled/{shippingId}")
    public ResponseEntity<String> changeStateToCancelled(@PathVariable Long shippingId) {
        try {
            shippingService.changeState(shippingId, State.CANCELADO);
            return ResponseEntity.ok("State updated successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiResponse(description = "Returns the top 3 most shipped products")
    @GetMapping(value = "reports/topSended")
    public ResponseEntity<List<Object[]>> getTop3() {
        List<Object[]> topShippedProducts = shippingService.getTop3();

        if (topShippedProducts.isEmpty()) {
            throw new ResourceNotFoundException("No top shipped products found.");
        }

        return ResponseEntity.ok(topShippedProducts);
    }



    @ApiResponse(description = "Creates a new shipping request")
    @PostMapping(value = "create")
    public ResponseEntity createShippingRequest(@RequestBody ShippingRequestDTO shippingRequestDTO) {
        try {
            return shippingService.createShippingRequest(shippingRequestDTO);
        } catch (BadRequestException e) {
            throw new BadRequestException("Invalid data provided for creating the shipping request: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while creating the shipping request: " + e.getMessage());
        }
    }


}
