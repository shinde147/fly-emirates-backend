package com.flyemirates.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="airports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Airport {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true,nullable = false)
	private String code;
	
	private String city;
	private String country;
	
	

}
