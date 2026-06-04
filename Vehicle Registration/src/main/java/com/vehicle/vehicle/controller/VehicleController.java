package com.vehicle.vehicle.controller;

import com.vehicle.vehicle.dto.VehicleRegistrationRequest;
import com.vehicle.vehicle.dto.VehicleResponse;
import com.vehicle.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

	private final VehicleService vehicleService;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public VehicleResponse register(@Valid @RequestBody VehicleRegistrationRequest request) {
		return vehicleService.register(request);
	}

	@GetMapping("/{id}")
	public VehicleResponse get(@PathVariable Long id) {
		return vehicleService.getById(id);
	}

	@GetMapping("/owner/{ownerId}")
	public List<VehicleResponse> listByOwner(@PathVariable Long ownerId) {
		return vehicleService.listByOwner(ownerId);
	}

	@PutMapping("/{id}/approve")
	public VehicleResponse approve(
			@PathVariable Long id, @RequestHeader("X-Admin-User-Id") Long adminUserId) {
		return vehicleService.approve(id, adminUserId);
	}

	@PutMapping("/{id}/reject")
	public VehicleResponse reject(
			@PathVariable Long id, @RequestHeader("X-Admin-User-Id") Long adminUserId) {
		return vehicleService.reject(id, adminUserId);
	}
}
