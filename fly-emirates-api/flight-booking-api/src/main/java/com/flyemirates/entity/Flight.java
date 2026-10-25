package com.flyemirates.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true,nullable = false)
	private String flightNo;
	
	@ManyToOne
	@JoinColumn(name="source_airport_id")
	private Airport source;
	
	@ManyToOne
	@JoinColumn(name="destination_airport_id")
	private Airport destination;
	
	private LocalDateTime departureTime;
	private LocalDateTime arrivalTime;
	private Double price;
	private Integer totalSeats;
	private Integer availableSeats;
	
	@Enumerated(EnumType.STRING)
	private FlightStatus status;

}
