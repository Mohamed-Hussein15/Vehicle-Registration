package com.vehicle.vehicle.dto;

import com.vehicle.vehicle.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppUserResponse {

	private final Long id;
	private final String username;
	private final String email;
	private final String fullName;
	private final UserRole role;
}
