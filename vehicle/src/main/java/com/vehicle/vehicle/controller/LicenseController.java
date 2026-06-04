package com.vehicle.vehicle.controller;

import com.vehicle.vehicle.dto.LicenseResponse;
import com.vehicle.vehicle.service.LicenseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/licenses")
@RequiredArgsConstructor
public class LicenseController {

	private final LicenseService licenseService;

	@PostMapping("/renew/{vehicleId}")
	public LicenseResponse renew(@PathVariable Long vehicleId) {
		return licenseService.renew(vehicleId);
	}

	@GetMapping("/{vehicleId:[0-9]+}")
	public LicenseResponse getForVehicle(@PathVariable Long vehicleId) {
		return licenseService.getForVehicle(vehicleId);
	}

	@GetMapping("/expired")
	public List<LicenseResponse> expired() {
		return licenseService.listExpired();
	}
}
