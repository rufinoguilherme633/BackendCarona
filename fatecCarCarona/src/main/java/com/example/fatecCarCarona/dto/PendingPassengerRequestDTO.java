package com.example.fatecCarCarona.dto;

public record PendingPassengerRequestDTO(
		Long id,
		String nomeMotorista,
		String foto,
		OriginDTO originDTO,
		DestinationDTO destinationDTO,
		Long id_carona,
		String status
		) {}
