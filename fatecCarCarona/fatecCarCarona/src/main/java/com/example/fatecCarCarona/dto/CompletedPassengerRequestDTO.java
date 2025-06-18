package com.example.fatecCarCarona.dto;

import java.time.LocalDateTime;

public record CompletedPassengerRequestDTO(
		Long id,
		OriginResponseDTO originDTO,
		DestinationResponseDTO destinationDTO,
		Long id_carona,
		LocalDateTime dataHora 
) {}
