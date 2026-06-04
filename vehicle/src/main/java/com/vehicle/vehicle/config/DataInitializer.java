package com.vehicle.vehicle.config;

import com.vehicle.vehicle.model.AppUser;
import com.vehicle.vehicle.model.UserRole;
import com.vehicle.vehicle.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

	private final AppUserRepository appUserRepository;

	@Override
	public void run(ApplicationArguments args) {
		if (appUserRepository.count() > 0) {
			return;
		}
		appUserRepository.save(AppUser.builder()
				.username("admin")
				.email("admin@local.test")
				.fullName("System Administrator")
				.role(UserRole.ADMIN)
				.build());
		appUserRepository.save(AppUser.builder()
				.username("jdoe")
				.email("jdoe@local.test")
				.fullName("Jane Owner")
				.role(UserRole.OWNER)
				.build());
	}
}
