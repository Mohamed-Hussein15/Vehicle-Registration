package com.vehicle.vehicle.controller;

import com.vehicle.vehicle.dto.InspectionRequest;
import com.vehicle.vehicle.dto.InspectionResponse;
import com.vehicle.vehicle.service.InspectionService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inspections")
@RequiredArgsConstructor
public class InspectionController {

	private final InspectionService inspectionService;

	@PostMapping("/{vehicleId}")
	@ResponseStatus(HttpStatus.CREATED)
	public InspectionResponse add(@PathVariable Long vehicleId, @Valid @RequestBody InspectionRequest request) {
		return inspectionService.add(vehicleId, request);
	}

	@GetMapping("/upcoming")
	public List<InspectionResponse> upcoming() {
		return inspectionService.upcoming();
	}
}
