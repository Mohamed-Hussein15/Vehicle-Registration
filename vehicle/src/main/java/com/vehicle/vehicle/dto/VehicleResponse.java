package com.vehicle.vehicle.dto;

import com.vehicle.vehicle.model.VehicleStatus;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VehicleResponse {

	private final Long id;
	private final Long ownerId;
	private final String plateNumber;
	private final String make;
	private final String model;
	private final Integer modelYear;
	private final VehicleStatus status;
	private final Instant registeredAt;
	private final Long approvedByUserId;
	private final Instant approvedAt;
}
