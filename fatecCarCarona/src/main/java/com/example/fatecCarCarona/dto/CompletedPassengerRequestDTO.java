package com.example.fatecCarCarona.dto;

public record CompletedPassengerRequestDTO(
		Long id,
		OriginResponseDTO originDTO,
		DestinationResponseDTO destinationDTO,
		Long id_carona		
) {}
