package com.vehicle.vehicle.service;

import com.vehicle.vehicle.model.AppUser;
import com.vehicle.vehicle.dto.AppUserResponse;
import com.vehicle.vehicle.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppUserService {

	private final AppUserRepository appUserRepository;

	@Transactional(readOnly = true)
	public AppUserResponse getById(Long id) {
		AppUser u = appUserRepository
				.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
		return new AppUserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getFullName(), u.getRole());
	}
}
