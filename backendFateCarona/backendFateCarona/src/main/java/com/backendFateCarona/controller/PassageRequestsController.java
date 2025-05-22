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

import com.backendFateCarona.dto.PassageRequestsDTO;
import com.backendFateCarona.dto.PassageRequestsRequestsDTO;
import com.backendFateCarona.dto.PassageRequestsUpdateDTO;
import com.backendFateCarona.dto.RideManagementDTO;
import com.backendFateCarona.dto.RideManagementRequestDTO;
import com.backendFateCarona.dto.RouteCoordinatesDTO;
import com.backendFateCarona.entity.RideManagement;
import com.backendFateCarona.dto.PassageRequestsUpdateDTO;
import com.backendFateCarona.service.HaversineService;
import com.backendFateCarona.service.PassageRequestsService;

@RestController
@RequestMapping("/solicitacao")
public class PassageRequestsController {
	@Autowired
	PassageRequestsService passageRequestsService;
	@Autowired
	HaversineService haversineService;
	@PostMapping
	public ResponseEntity<PassageRequestsDTO> createRide(@RequestBody PassageRequestsRequestsDTO request ) throws Exception{
		PassageRequestsDTO newPassage = passageRequestsService.createPassageRequests(request);
		return new ResponseEntity<>(newPassage,HttpStatus.CREATED);

	}
	
	@PatchMapping("/{id_solicitacao}")
	public ResponseEntity<PassageRequestsDTO> updateRide(@PathVariable(name = "id_solicitacao") Long id_solicitacao, @RequestBody PassageRequestsUpdateDTO request ) throws Exception{
		PassageRequestsDTO passage = passageRequestsService.updatePassageRequests(id_solicitacao, request);
		return new ResponseEntity<>(passage,HttpStatus.OK);

	}
	
	@DeleteMapping("/{id_solicitacao}")
	public ResponseEntity<Map<String, String>> deleteRide(@PathVariable Long id_solicitacao) {
	    passageRequestsService.deletePassageRequests(id_solicitacao);
	    
	    Map<String, String> response = new HashMap<>();
	    response.put("message", "Solicitação deletada com sucesso!");

	    return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/{id_passageiro}")
	public ResponseEntity<PassageRequestsUpdateDTO>  getAllRequests(@PathVariable(name = "id_passageiro") Long id_passageiro) throws Exception{
		List<PassageRequestsUpdateDTO> rides = passageRequestsService.getAllRequests(id_passageiro);
		return new ResponseEntity(rides,HttpStatus.OK);
	}
	
	@GetMapping("/proximos")
	public ResponseEntity<List<RideManagement>> findNearbyDrivers(@RequestBody RouteCoordinatesDTO routeCoordinatesDTO) throws Exception{
		List<RideManagement> motoristaProximos = haversineService.findNearbyDrivers(routeCoordinatesDTO);
		
		return new ResponseEntity(motoristaProximos,HttpStatus.OK);
	
	}
}
