package com.backendFateCarona.dto;

import java.time.LocalDateTime;

import com.backendFateCarona.entity.RideStatus;

public record RideManagementRequestUpdateDTO(
		Long motorista,
		String origem,
		String destino,
		LocalDateTime dataHora,
		Integer vagasDisponiveis,
		RideStatus status
		)
{}
