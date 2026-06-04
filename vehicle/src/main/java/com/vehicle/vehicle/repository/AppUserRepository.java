package com.vehicle.vehicle.repository;

import com.vehicle.vehicle.model.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	Optional<AppUser> findByUsername(String username);

	boolean existsByUsernameOrEmail(String username, String email);
}
