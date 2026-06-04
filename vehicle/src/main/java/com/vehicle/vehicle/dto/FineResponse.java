package com.vehicle.vehicle.dto;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FineResponse {

	private final Long id;
	private final Long ownerId;
	private final Long vehicleId;
	private final BigDecimal amount;
	private final String reason;
	private final boolean paid;
	private final Instant createdAt;
}
