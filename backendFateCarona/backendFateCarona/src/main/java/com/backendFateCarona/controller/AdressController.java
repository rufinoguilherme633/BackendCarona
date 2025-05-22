package com.backendFateCarona.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backendFateCarona.service.OpenstreetmapService;
import com.backendFateCarona.dto.AdressDTO;
import com.backendFateCarona.dto.DriverDTO;
import com.backendFateCarona.dto.DriverDTO;
import com.backendFateCarona.dto.DriverDTO;
import com.backendFateCarona.dto.DriverDTO;
import com.backendFateCarona.dto.DriverDTO;

@RestController
@RequestMapping
public class AdressController {
	@Autowired
	OpenstreetmapService openstreetmapService;
	@GetMapping("/local")
	public ResponseEntity<?> buscarLocal(@RequestParam String local) {
		
		Optional<AdressDTO> nominatimResult  = openstreetmapService.buscarLocal(local);
		
		if (nominatimResult.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum endereço encontrado");
		}
		return  ResponseEntity.status(HttpStatus.OK).body(nominatimResult);

	}
}
