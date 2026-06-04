package com.vehicle.vehicle.dto;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

	private final Instant timestamp;
	private final int status;
	private final String error;
	private final String message;
	private final List<String> details;
}
