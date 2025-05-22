package com.backendFateCarona.dto;

import java.time.LocalDateTime;

import com.backendFateCarona.entity.RequestStatus;

public record PassageRequestsRequestsDTO(
		Long carona,
		Long passageiro,
		LocalDateTime dataHora,
		String origem,
		String destino
) {}
