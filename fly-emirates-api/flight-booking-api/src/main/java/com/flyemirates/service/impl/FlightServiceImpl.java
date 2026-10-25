package com.flyemirates.service.impl;

import com.flyemirates.dto.FlightRequest;
import com.flyemirates.dto.FlightResponse;
import com.flyemirates.entity.Airport;
import com.flyemirates.entity.Booking;          // NEW
import com.flyemirates.entity.BookingStatus;    // NEW
import com.flyemirates.entity.Flight;
import com.flyemirates.entity.FlightStatus;
import com.flyemirates.repository.AirportRepository;
import com.flyemirates.repository.BookingRepository;   // NEW
import com.flyemirates.repository.FlightRepository;
import com.flyemirates.service.FlightService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;   // NEW

import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final BookingRepository bookingRepository;   // NEW

    public FlightServiceImpl(FlightRepository flightRepository,
                             AirportRepository airportRepository,
                             BookingRepository bookingRepository) {   // NEW
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
        this.bookingRepository = bookingRepository;   // NEW
    }

    @Override
    public FlightResponse createFlight(FlightRequest request) {
        Airport source = airportRepository.findByCode(request.getSourceAirportCode())
                .orElseThrow(() -> new RuntimeException("Source airport not found: " + request.getSourceAirportCode()));
        Airport destination = airportRepository.findByCode(request.getDestinationAirportCode())
                .orElseThrow(() -> new RuntimeException("Destination airport not found: " + request.getDestinationAirportCode()));

        Flight flight = new Flight();
        flight.setFlightNo(request.getFlightNo());
        flight.setSource(source);
        flight.setDestination(destination);
        flight.setDepartureTime(request.getDepartureTime());
        flight.setArrivalTime(request.getArrivalTime());
        flight.setPrice(request.getPrice());
        flight.setTotalSeats(request.getTotalSeats());
        flight.setAvailableSeats(request.getTotalSeats()); // all seats available at creation
        flight.setStatus(FlightStatus.SCHEDULED);

        Flight saved = flightRepository.save(flight);
        return mapToResponse(saved);
    }

    @Override
    public FlightResponse updateFlight(Long id, FlightRequest request) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found: " + id));

        Airport source = airportRepository.findByCode(request.getSourceAirportCode())
                .orElseThrow(() -> new RuntimeException("Source airport not found"));
        Airport destination = airportRepository.findByCode(request.getDestinationAirportCode())
                .orElseThrow(() -> new RuntimeException("Destination airport not found"));

        flight.setFlightNo(request.getFlightNo());
        flight.setSource(source);
        flight.setDestination(destination);
        flight.setDepartureTime(request.getDepartureTime());
        flight.setArrivalTime(request.getArrivalTime());
        flight.setPrice(request.getPrice());
        flight.setTotalSeats(request.getTotalSeats());

        Flight updated = flightRepository.save(flight);
        return mapToResponse(updated);
    }

    @Override
    @Transactional   // NEW: flight + bookings change together, or not at all
    public void deleteFlight(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found: " + id));

        // NEW: stop a second cancellation
        if (flight.getStatus() == FlightStatus.CANCELLED) {
            throw new RuntimeException("Flight is already cancelled");
        }

        flight.setStatus(FlightStatus.CANCELLED); // soft delete, keeps booking history intact
        flightRepository.save(flight);

        // NEW: cancel every active booking on this flight
        List<Booking> activeBookings =
                bookingRepository.findByFlightAndStatus(flight, BookingStatus.BOOKED);
        for (Booking booking : activeBookings) {
            booking.setStatus(BookingStatus.CANCELLED);
        }
        bookingRepository.saveAll(activeBookings);
    }

    @Override
    public FlightResponse getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found: " + id));
        return mapToResponse(flight);
    }

    @Override
    public List<FlightResponse> getAllFlights() {
        return flightRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FlightResponse> searchFlights(String sourceCode, String destinationCode) {
        return flightRepository.findBySource_CodeAndDestination_Code(sourceCode, destinationCode).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private FlightResponse mapToResponse(Flight flight) {
        return new FlightResponse(
                flight.getId(),
                flight.getFlightNo(),
                flight.getSource().getCode(),
                flight.getSource().getCity(),
                flight.getDestination().getCode(),
                flight.getDestination().getCity(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getPrice(),
                flight.getTotalSeats(),
                flight.getAvailableSeats(),
                flight.getStatus().name()
        );
    }
}