package com.backendFateCarona.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backendFateCarona.dto.RideManagementRequestUpdateDTO;
import com.backendFateCarona.entity.RideManagement;
import com.backendFateCarona.entity.User;

public interface RideManagementRepository extends JpaRepository<RideManagement, Long>{

	List<RideManagement> findAllByMotorista(User motorista);

}
