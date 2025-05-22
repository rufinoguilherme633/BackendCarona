package com.backendFateCarona.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backendFateCarona.dto.VehicleDTO;
import com.backendFateCarona.entity.User;
import com.backendFateCarona.entity.Vehicle;

public interface VehiceRepositore extends JpaRepository<Vehicle, Long>{

	Optional<Vehicle> findByPlaca(String placa);

	Optional<Vehicle> findByUsuario(User usuario); 

}
