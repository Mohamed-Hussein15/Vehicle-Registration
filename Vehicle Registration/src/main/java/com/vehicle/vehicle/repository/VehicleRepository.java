package com.vehicle.vehicle.repository;

import com.vehicle.vehicle.model.Vehicle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	boolean existsByPlateNumberIgnoreCase(String plateNumber);

	List<Vehicle> findByOwnerIdOrderByRegisteredAtDesc(Long ownerId);
}
