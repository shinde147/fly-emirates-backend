package com.flyemirates.service.impl;

import com.flyemirates.dto.*;
import com.flyemirates.entity.*;
import com.flyemirates.repository.BookingRepository;
import com.flyemirates.repository.FlightRepository;
import com.flyemirates.repository.UserRepository;
import com.flyemirates.service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, FlightRepository flightRepository,
                               UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found: " + request.getFlightId()));

        int seatsNeeded = request.getPassengers().size();

        if (flight.getAvailableSeats() < seatsNeeded) {
            throw new RuntimeException("Not enough seats available. Only " + flight.getAvailableSeats() + " left.");
        }

        if (flight.getStatus() == FlightStatus.CANCELLED) {
            throw new RuntimeException("Cannot book a cancelled flight");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setStatus(BookingStatus.BOOKED);
        booking.setAmountPaid(flight.getPrice() * seatsNeeded);

        for (PassengerRequest pr : request.getPassengers()) {
            Passenger passenger = new Passenger();
            passenger.setName(pr.getName());
            passenger.setAge(pr.getAge());
            passenger.setGender(pr.getGender());
            passenger.setBooking(booking);
            booking.getPassengers().add(passenger);
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - seatsNeeded);
        flightRepository.save(flight);

        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
    }

    @Override
    public List<BookingResponse> getMyBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bookingRepository.findByUser(user).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You are not authorized to cancel this booking");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        Flight flight = booking.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + booking.getPassengers().size());

        bookingRepository.save(booking);
        flightRepository.save(flight);
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private BookingResponse mapToResponse(Booking booking) {
        List<PassengerResponse> passengerResponses = booking.getPassengers().stream()
                .map(p -> new PassengerResponse(p.getId(), p.getName(), p.getAge(), p.getGender()))
                .toList();

        return new BookingResponse(
                booking.getId(),
                booking.getFlight().getFlightNo(),
                booking.getFlight().getSource().getCity(),
                booking.getFlight().getDestination().getCity(),
                booking.getBookingDate(),
                booking.getStatus().name(),
                booking.getAmountPaid(),
                passengerResponses
        );
    }
}
