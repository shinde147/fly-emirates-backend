package com.flyemirates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="passengers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="booking_id")
	private Booking booking;
	
	private String name;
	private Integer age;
	private String gender;
	
	

}
