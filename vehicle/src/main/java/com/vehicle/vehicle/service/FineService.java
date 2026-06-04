package com.vehicle.vehicle.service;

import com.vehicle.vehicle.model.Fine;
import com.vehicle.vehicle.dto.FineResponse;
import com.vehicle.vehicle.repository.AppUserRepository;
import com.vehicle.vehicle.repository.FineRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FineService {

	private final FineRepository fineRepository;
	private final AppUserRepository appUserRepository;

	@Transactional(readOnly = true)
	public List<FineResponse> listByOwner(Long ownerId) {
		if (!appUserRepository.existsById(ownerId)) {
			throw new IllegalArgumentException("Owner user not found: " + ownerId);
		}
		return fineRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId).stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

	@Transactional
	public FineResponse pay(Long fineId) {
		Fine fine = fineRepository
				.findById(fineId)
				.orElseThrow(() -> new IllegalArgumentException("Fine not found: " + fineId));
		if (fine.isPaid()) {
			throw new IllegalArgumentException("Fine is already paid.");
		}
		fine.setPaid(true);
		fine = fineRepository.save(fine);
		return toResponse(fine);
	}

	private FineResponse toResponse(Fine f) {
		f.getOwner().getId();
		Long vehicleId = f.getVehicle() != null ? f.getVehicle().getId() : null;
		return new FineResponse(
				f.getId(), f.getOwner().getId(), vehicleId, f.getAmount(), f.getReason(), f.isPaid(), f.getCreatedAt());
	}
}
