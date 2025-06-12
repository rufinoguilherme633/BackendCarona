package com.example.fatecCarCarona.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PutExchange;

import com.example.fatecCarCarona.dto.RideDTO;
import com.example.fatecCarCarona.dto.RideResponseDTO;
import com.example.fatecCarCarona.service.RideService;
import com.example.fatecCarCarona.service.TokenService;

@RestController
@RequestMapping("/rides")
public class RideController {
	  @Autowired
	    private RideService rideService;
	  @Autowired
	  private TokenService tokenService;

	    // Exemplo: POST /rides?userId=1
	    @PostMapping
	    public ResponseEntity<RideDTO> createRide(@RequestHeader("Authorization") String authHeader, @RequestBody RideDTO rideDTO) throws Exception {
			Long idLong = tokenService.extractUserIdFromHeader(authHeader);

			RideDTO ride = rideService.PostRide(idLong, rideDTO);
	    	return ResponseEntity.ok(ride);
	    }


	    @GetMapping("/corridasAtivas")
	    public ResponseEntity<List<RideResponseDTO>> findByRidesAtivasByUser(@RequestHeader("Authorization") String authHeader) {
	    	Long idLong = tokenService.extractUserIdFromHeader(authHeader);
	    	List<RideResponseDTO> caronaDetalhadaProjection = rideService.findAtivasByDriverId(idLong);
	    	return ResponseEntity.ok(caronaDetalhadaProjection);
	    }


	    @GetMapping("/concluidas")
	    public ResponseEntity<Page<RideResponseDTO>> getCaronasConcluidasByDriver(
	    		@RequestHeader("Authorization") String authHeader,
	            @RequestParam(defaultValue = "0") int pagina,
	            @RequestParam(defaultValue = "10") int itens) throws Exception {

	    	Long idLong = tokenService.extractUserIdFromHeader(authHeader);
	        Page<RideResponseDTO> rides = rideService.findConcluidasyDriverId(idLong, pagina, itens);
	        return ResponseEntity.ok(rides);
	    }


	    @PutExchange("cancelar/{rideId}")
	    public ResponseEntity<String> cancelRideByDriver(@RequestHeader("Authorization") String authHeader, @PathVariable Long rideId) {

	    		Long idLong = tokenService.extractUserIdFromHeader(authHeader);

	            rideService.cancelRideByDriver(idLong, rideId);
	            return ResponseEntity.ok("Carona cancelada com sucesso.");

	    }

	    @PutExchange("/{rideId}")
	    public ResponseEntity<RideDTO> atualizarDriverRotas(@RequestHeader("Authorization") String authHeader, @PathVariable Long rideId, @RequestBody RideDTO rideDTO) throws Exception {

	    		Long idLong = tokenService.extractUserIdFromHeader(authHeader);

	    		RideDTO ride = rideService.atualizarDriverRotas(idLong, rideDTO, rideId);
	            return ResponseEntity.ok(ride);

	    }
	}

