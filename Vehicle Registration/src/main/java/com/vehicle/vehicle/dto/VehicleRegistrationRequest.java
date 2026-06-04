package com.vehicle.vehicle.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehicleRegistrationRequest {

	@NotNull
	private Long ownerUserId;

	@NotBlank
	@Size(max = 32)
	private String plateNumber;

	@NotBlank
	@Size(max = 80)
	private String make;

	@NotBlank
	@Size(max = 80)
	private String model;

	@NotNull
	@Min(1900)
	@Max(2100)
	private Integer modelYear;
}
