package com.flyemirates.service;

import com.flyemirates.dto.BookingRequest;
import com.flyemirates.dto.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request, String userEmail);
    List<BookingResponse> getMyBookings(String userEmail);
    void cancelBooking(Long bookingId, String userEmail);
    List<BookingResponse> getAllBookings(); // for admin/staff
}