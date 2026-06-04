package com.vehicle.vehicle.controller;

import com.vehicle.vehicle.dto.AppUserResponse;
import com.vehicle.vehicle.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final AppUserService appUserService;

	@GetMapping("/{id}")
	public AppUserResponse get(@PathVariable Long id) {
		return appUserService.getById(id);
	}
}
