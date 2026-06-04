package com.vehicle.vehicle.repository;

import com.vehicle.vehicle.model.VehicleLicense;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VehicleLicenseRepository extends JpaRepository<VehicleLicense, Long> {

	Optional<VehicleLicense> findTopByVehicleIdOrderByExpiryDateDesc(Long vehicleId);

	@Query(
			"select l from VehicleLicense l where l.expiryDate < :today and l.expiryDate = "
					+ "(select max(l2.expiryDate) from VehicleLicense l2 where l2.vehicle = l.vehicle)")
	List<VehicleLicense> findLatestExpiredPerVehicle(LocalDate today);
}
