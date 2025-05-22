package com.backendFateCarona.dto;

import java.time.LocalDateTime;

import com.backendFateCarona.entity.RideStatus;
import com.backendFateCarona.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;

public record RideManagementDTO(
		Long motorista,
		String origem,
		Double longitudeOrigem,
		Double latitudeOrigem,
		String destino,
		Double longitudeDestino,
		Double latitudeDestino,
		LocalDateTime dataHora,
		Integer vagasDisponiveis,
		RideStatus status
) {}
