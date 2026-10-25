package com.flyemirates.repository;

import com.flyemirates.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findByFlightNo(String flightNo);
    List<Flight> findBySource_CodeAndDestination_Code(String sourceCode, String destinationCode);
}