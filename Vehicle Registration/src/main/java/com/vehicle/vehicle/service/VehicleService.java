package com.vehicle.vehicle.service;

import com.vehicle.vehicle.model.AppUser;
import com.vehicle.vehicle.model.UserRole;
import com.vehicle.vehicle.model.Vehicle;
import com.vehicle.vehicle.model.VehicleStatus;
import com.vehicle.vehicle.dto.VehicleRegistrationRequest;
import com.vehicle.vehicle.dto.VehicleResponse;
import com.vehicle.vehicle.repository.AppUserRepository;
import com.vehicle.vehicle.repository.VehicleRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VehicleService {

	private final VehicleRepository vehicleRepository;
	private final AppUserRepository appUserRepository;
	private final LicenseService licenseService;

	@Transactional
	public VehicleResponse register(VehicleRegistrationRequest request) {
		AppUser owner = appUserRepository
				.findById(request.getOwnerUserId())
				.orElseThrow(() -> new IllegalArgumentException("Owner user not found: " + request.getOwnerUserId()));
		if (owner.getRole() != UserRole.OWNER) {
			throw new IllegalArgumentException("Only users with role OWNER can own vehicles.");
		}
		if (vehicleRepository.existsByPlateNumberIgnoreCase(request.getPlateNumber())) {
			throw new IllegalArgumentException("Plate number already registered: " + request.getPlateNumber());
		}
		Vehicle vehicle = Vehicle.builder()
				.owner(owner)
				.plateNumber(request.getPlateNumber().trim().toUpperCase())
				.make(request.getMake().trim())
				.model(request.getModel().trim())
				.modelYear(request.getModelYear())
				.status(VehicleStatus.PENDING)
				.registeredAt(Instant.now())
				.build();
		vehicle = vehicleRepository.save(vehicle);
		return VehicleMapper.toResponse(vehicle);
	}

	@Transactional(readOnly = true)
	public VehicleResponse getById(Long id) {
		Vehicle v = vehicleRepository
				.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + id));
		v.getOwner().getId();
		if (v.getApprovedBy() != null) {
			v.getApprovedBy().getId();
		}
		return VehicleMapper.toResponse(v);
	}

	@Transactional(readOnly = true)
	public List<VehicleResponse> listByOwner(Long ownerId) {
		if (!appUserRepository.existsById(ownerId)) {
			throw new IllegalArgumentException("Owner user not found: " + ownerId);
		}
		return vehicleRepository.findByOwnerIdOrderByRegisteredAtDesc(ownerId).stream()
				.map(VehicleMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Transactional
	public VehicleResponse approve(Long vehicleId, Long adminUserId) {
		AppUser admin = requireAdmin(adminUserId);
		Vehicle v = vehicleRepository
				.findById(vehicleId)
				.orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + vehicleId));
		if (v.getStatus() != VehicleStatus.PENDING) {
			throw new IllegalArgumentException("Vehicle is not pending approval.");
		}
		v.setStatus(VehicleStatus.APPROVED);
		v.setApprovedBy(admin);
		v.setApprovedAt(Instant.now());
		vehicleRepository.save(v);
		LocalDate issued = LocalDate.now();
		licenseService.issueInitialLicense(v, issued, issued.plusYears(1));
		return VehicleMapper.toResponse(v);
	}

	@Transactional
	public VehicleResponse reject(Long vehicleId, Long adminUserId) {
		requireAdmin(adminUserId);
		Vehicle v = vehicleRepository
				.findById(vehicleId)
				.orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + vehicleId));
		if (v.getStatus() != VehicleStatus.PENDING) {
			throw new IllegalArgumentException("Vehicle is not pending approval.");
		}
		v.setStatus(VehicleStatus.REJECTED);
		v.setApprovedBy(null);
		v.setApprovedAt(null);
		vehicleRepository.save(v);
		return VehicleMapper.toResponse(v);
	}

	private AppUser requireAdmin(Long adminUserId) {
		AppUser admin = appUserRepository
				.findById(adminUserId)
				.orElseThrow(() -> new IllegalArgumentException("Admin user not found: " + adminUserId));
		if (admin.getRole() != UserRole.ADMIN) {
			throw new IllegalArgumentException("User " + adminUserId + " is not an ADMIN.");
		}
		return admin;
	}
}
