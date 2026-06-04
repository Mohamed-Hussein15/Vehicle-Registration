package com.vehicle.vehicle.repository;

import com.vehicle.vehicle.model.InspectionResult;
import com.vehicle.vehicle.model.VehicleInspection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VehicleInspectionRepository extends JpaRepository<VehicleInspection, Long> {

	Optional<VehicleInspection> findTopByVehicleIdOrderByInspectionDateDesc(Long vehicleId);

	@Query(
			"select i from VehicleInspection i where i.nextDueDate between :from and :to order by i.nextDueDate asc")
	List<VehicleInspection> findUpcomingNextDueBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

	Optional<VehicleInspection> findTopByVehicleIdAndResultOrderByInspectionDateDesc(Long vehicleId, InspectionResult result);
}
