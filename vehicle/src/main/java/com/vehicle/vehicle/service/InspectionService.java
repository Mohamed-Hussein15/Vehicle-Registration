package com.vehicle.vehicle.service;

import com.vehicle.vehicle.model.Vehicle;
import com.vehicle.vehicle.model.VehicleInspection;
import com.vehicle.vehicle.model.VehicleStatus;
import com.vehicle.vehicle.dto.InspectionRequest;
import com.vehicle.vehicle.dto.InspectionResponse;
import com.vehicle.vehicle.repository.VehicleInspectionRepository;
import com.vehicle.vehicle.repository.VehicleRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InspectionService {

	private final VehicleRepository vehicleRepository;
	private final VehicleInspectionRepository vehicleInspectionRepository;

	@Value("${vrl.inspection.upcoming-days}")
	private int upcomingWithinDays;

	@Transactional
	public InspectionResponse add(Long vehicleId, InspectionRequest request) {
		Vehicle vehicle = vehicleRepository
				.findById(vehicleId)
				.orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + vehicleId));
		if (vehicle.getStatus() != VehicleStatus.APPROVED) {
			throw new IllegalArgumentException("Inspections can only be recorded for APPROVED vehicles.");
		}
		LocalDate inspectionDate =
				request.getInspectionDate() != null ? request.getInspectionDate() : LocalDate.now();
		LocalDate nextDue = inspectionDate.plusMonths(6);
		VehicleInspection row = VehicleInspection.builder()
				.vehicle(vehicle)
				.inspectionDate(inspectionDate)
				.result(request.getResult())
				.nextDueDate(nextDue)
				.build();
		row = vehicleInspectionRepository.save(row);
		return toResponse(row);
	}

	@Transactional(readOnly = true)
	public List<InspectionResponse> upcoming() {
		LocalDate from = LocalDate.now();
		LocalDate to = from.plusDays(upcomingWithinDays);
		return vehicleInspectionRepository.findUpcomingNextDueBetween(from, to).stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

	private InspectionResponse toResponse(VehicleInspection i) {
		i.getVehicle().getId();
		return new InspectionResponse(
				i.getId(), i.getVehicle().getId(), i.getInspectionDate(), i.getResult(), i.getNextDueDate());
	}
}
