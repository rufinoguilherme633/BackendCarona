package com.backendFateCarona.dto;

import java.time.LocalDateTime;

import com.backendFateCarona.entity.RideStatus;

public record RideManagementRequestDTO(
		Long motorista,
		String origem,
		String destino,
		LocalDateTime dataHora,
		Integer vagasDisponiveis
		
		) 
{}
