package com.vehicle.vehicle.dto;

import com.vehicle.vehicle.model.InspectionResult;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InspectionRequest {

	private LocalDate inspectionDate;

	@NotNull
	private InspectionResult result;
}
