package com.flyemirates.repository;

import com.flyemirates.entity.Booking;
import com.flyemirates.entity.BookingStatus;
import com.flyemirates.entity.Flight;
import com.flyemirates.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByFlightAndStatus(Flight flight, BookingStatus status); // NEW
}