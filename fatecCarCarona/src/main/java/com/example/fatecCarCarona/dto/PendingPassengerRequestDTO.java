package com.example.fatecCarCarona.dto;

public record PendingPassengerRequestDTO(
		Long id,
		OriginDTO originDTO,
		DestinationDTO destinationDTO,
		Long id_carona,
		String status
		) {}
