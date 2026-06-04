package com.vehicle.vehicle.repository;

import com.vehicle.vehicle.model.Fine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRepository extends JpaRepository<Fine, Long> {

	List<Fine> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);
}
