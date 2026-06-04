package com.vehicle.vehicle.dto;

import com.vehicle.vehicle.model.InspectionResult;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InspectionResponse {

	private final Long id;
	private final Long vehicleId;
	private final LocalDate inspectionDate;
	private final InspectionResult result;
	private final LocalDate nextDueDate;
}
