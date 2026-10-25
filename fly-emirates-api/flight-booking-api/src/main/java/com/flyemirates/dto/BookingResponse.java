package com.flyemirates.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private String flightNo;
    private String sourceCity;
    private String destinationCity;
    private LocalDateTime bookingDate;
    private String status;
    private Double amountPaid;
    private List<PassengerResponse> passengers;
}