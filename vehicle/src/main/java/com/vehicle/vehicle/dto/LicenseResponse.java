package com.vehicle.vehicle.dto;

import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LicenseResponse {

	private final Long id;
	private final Long vehicleId;
	private final LocalDate issuedDate;
	private final LocalDate expiryDate;
	private final Instant createdAt;
}
