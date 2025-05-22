package com.backendFateCarona.dto;

import java.time.LocalDateTime;

import com.backendFateCarona.entity.RequestStatus;

public record PassageRequestsDTO(
		Long carona,
		Long passageiro,
		LocalDateTime dataHora,
		String origem,
		Double longitudeOrigem,
		Double latitudeOrigem,
		String destino,
		Double longitudeDestino,
		Double latitudeDestino,
		RequestStatus status		
) {}
