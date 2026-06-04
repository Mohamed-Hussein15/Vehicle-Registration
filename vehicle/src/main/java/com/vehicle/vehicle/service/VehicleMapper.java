package com.vehicle.vehicle.service;

import com.vehicle.vehicle.model.Vehicle;
import com.vehicle.vehicle.dto.VehicleResponse;

final class VehicleMapper {

	private VehicleMapper() {
	}

	static VehicleResponse toResponse(Vehicle v) {
		Long approvedBy = v.getApprovedBy() != null ? v.getApprovedBy().getId() : null;
		return new VehicleResponse(
				v.getId(),
				v.getOwner().getId(),
				v.getPlateNumber(),
				v.getMake(),
				v.getModel(),
				v.getModelYear(),
				v.getStatus(),
				v.getRegisteredAt(),
				approvedBy,
				v.getApprovedAt());
	}
}
