package com.flyemirates.service;

import com.flyemirates.dto.FlightRequest;
import com.flyemirates.dto.FlightResponse;

import java.util.List;

public interface FlightService {
    FlightResponse createFlight(FlightRequest request);
    FlightResponse updateFlight(Long id, FlightRequest request);
    void deleteFlight(Long id);
    FlightResponse getFlightById(Long id);
    List<FlightResponse> getAllFlights();
    List<FlightResponse> searchFlights(String sourceCode, String destinationCode);
}