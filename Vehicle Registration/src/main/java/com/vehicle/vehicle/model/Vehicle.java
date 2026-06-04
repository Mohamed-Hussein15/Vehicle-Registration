package com.vehicle.vehicle.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "VRL_VEHICLE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "owner_id", nullable = false)
	private AppUser owner;

	@Column(nullable = false, unique = true, length = 32)
	private String plateNumber;

	@Column(nullable = false, length = 80)
	private String make;

	@Column(nullable = false, length = 80)
	private String model;

	@Column(nullable = false)
	private Integer modelYear;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private VehicleStatus status;

	@Column(nullable = false)
	private Instant registeredAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by_id")
	private AppUser approvedBy;

	private Instant approvedAt;
}
