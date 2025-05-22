package com.backendFateCarona.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backendFateCarona.dto.RideManagementDTO;
import com.backendFateCarona.dto.RideManagementRequestDTO;
import com.backendFateCarona.dto.RideManagementRequestUpdateDTO;
import com.backendFateCarona.entity.RideManagement;
import com.backendFateCarona.service.RideManagementService;

@RestController
@RequestMapping("/criarCarona")
public class RideManagementController {
	
	@Autowired
	RideManagementService rideManagementService;
	
	@PostMapping
	public ResponseEntity<RideManagementDTO> createRide(@RequestBody RideManagementRequestDTO ride ) throws Exception{
		RideManagementDTO newRide = rideManagementService.createRide(ride);
		return new ResponseEntity<>(newRide,HttpStatus.CREATED);

	}
	@PatchMapping("/{id_carona}")
	public ResponseEntity<RideManagementDTO> updateRide(@PathVariable(name = "id_carona") Long id_carona, @RequestBody RideManagementRequestUpdateDTO ride ) throws Exception{
		RideManagementDTO newRide = rideManagementService.updateRide(id_carona, ride);
		return new ResponseEntity<>(newRide,HttpStatus.OK);

	}
	
	@DeleteMapping("/{id_carona}")
	public ResponseEntity<Map<String, String>> deleteRide(@PathVariable Long id_carona) {
	    rideManagementService.deleteRide(id_carona);
	    
	    Map<String, String> response = new HashMap<>();
	    response.put("message", "Carona deletada com sucesso!");

	    return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/{motorista}")
	public ResponseEntity<RideManagementRequestUpdateDTO>  getAllRides(@PathVariable(name = "motorista") Long motorista) throws Exception{
		List<RideManagementRequestUpdateDTO> rides = rideManagementService.getAllRides(motorista);
		return new ResponseEntity(rides,HttpStatus.OK);
	}

}
