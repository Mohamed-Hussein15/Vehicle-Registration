package com.vehicle.vehicle.service;

import com.vehicle.vehicle.model.Fine;
import com.vehicle.vehicle.model.InspectionResult;
import com.vehicle.vehicle.model.Vehicle;
import com.vehicle.vehicle.model.VehicleInspection;
import com.vehicle.vehicle.model.VehicleLicense;
import com.vehicle.vehicle.model.VehicleStatus;
import com.vehicle.vehicle.dto.LicenseResponse;
import com.vehicle.vehicle.repository.FineRepository;
import com.vehicle.vehicle.repository.VehicleInspectionRepository;
import com.vehicle.vehicle.repository.VehicleLicenseRepository;
import com.vehicle.vehicle.repository.VehicleRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LicenseService {

	private final VehicleRepository vehicleRepository;
	private final VehicleLicenseRepository vehicleLicenseRepository;
	private final VehicleInspectionRepository vehicleInspectionRepository;
	private final FineRepository fineRepository;

	@Value("${vrl.fine.amount}")
	private BigDecimal lateRenewalAmount;

	@Transactional
	public void issueInitialLicense(Vehicle vehicle, LocalDate issuedDate, LocalDate expiryDate) {
		VehicleLicense license = VehicleLicense.builder()
				.vehicle(vehicle)
				.issuedDate(issuedDate)
				.expiryDate(expiryDate)
				.createdAt(Instant.now())
				.build();
		vehicleLicenseRepository.save(license);
	}

	@Transactional
	public LicenseResponse renew(Long vehicleId) {
		Vehicle vehicle = vehicleRepository
				.findById(vehicleId)
				.orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + vehicleId));
		if (vehicle.getStatus() != VehicleStatus.APPROVED) {
			throw new IllegalArgumentException("Vehicle must be APPROVED to renew license.");
		}
		VehicleInspection latestPass = vehicleInspectionRepository
				.findTopByVehicleIdAndResultOrderByInspectionDateDesc(vehicleId, InspectionResult.PASS)
				.orElseThrow(() -> new IllegalArgumentException(
						"A passing inspection is required. Add a PASS inspection before renewal."));
		LocalDate today = LocalDate.now();
		if (latestPass.getInspectionDate().isBefore(today.minusMonths(6))) {
			throw new IllegalArgumentException(
					"Inspection is older than 6 months. A new passing inspection is required before renewal.");
		}
		VehicleLicense current = vehicleLicenseRepository
				.findTopByVehicleIdOrderByExpiryDateDesc(vehicleId)
				.orElseThrow(() -> new IllegalArgumentException("No license on file for this vehicle."));
		boolean late = today.isAfter(current.getExpiryDate());
		if (late) {
			Fine fine = Fine.builder()
					.owner(vehicle.getOwner())
					.vehicle(vehicle)
					.amount(lateRenewalAmount)
					.reason("Late license renewal after expiry on " + current.getExpiryDate())
					.paid(false)
					.createdAt(Instant.now())
					.build();
			fineRepository.save(fine);
		}
		LocalDate issued = today;
		LocalDate expiry = issued.plusYears(1);
		VehicleLicense next = VehicleLicense.builder()
				.vehicle(vehicle)
				.issuedDate(issued)
				.expiryDate(expiry)
				.createdAt(Instant.now())
				.build();
		next = vehicleLicenseRepository.save(next);
		return toResponse(next);
	}

	@Transactional(readOnly = true)
	public LicenseResponse getForVehicle(Long vehicleId) {
		if (!vehicleRepository.existsById(vehicleId)) {
			throw new IllegalArgumentException("Vehicle not found: " + vehicleId);
		}
		return vehicleLicenseRepository
				.findTopByVehicleIdOrderByExpiryDateDesc(vehicleId)
				.map(this::toResponse)
				.orElseThrow(() -> new IllegalArgumentException("No license on file for this vehicle."));
	}

	@Transactional(readOnly = true)
	public List<LicenseResponse> listExpired() {
		return vehicleLicenseRepository.findLatestExpiredPerVehicle(LocalDate.now()).stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

	private LicenseResponse toResponse(VehicleLicense l) {
		l.getVehicle().getId();
		return new LicenseResponse(l.getId(), l.getVehicle().getId(), l.getIssuedDate(), l.getExpiryDate(), l.getCreatedAt());
	}
}
