package com.vehicle.vehicle.controller;

import com.vehicle.vehicle.dto.FineResponse;
import com.vehicle.vehicle.service.FineService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fines")
@RequiredArgsConstructor
public class FineController {

	private final FineService fineService;

	@GetMapping("/{ownerId}")
	public List<FineResponse> listForOwner(@PathVariable Long ownerId) {
		return fineService.listByOwner(ownerId);
	}

	@PostMapping("/pay/{fineId}")
	public FineResponse pay(@PathVariable Long fineId) {
		return fineService.pay(fineId);
	}
}
