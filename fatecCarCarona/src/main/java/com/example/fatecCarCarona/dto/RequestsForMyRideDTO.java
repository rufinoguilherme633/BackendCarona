package com.example.fatecCarCarona.dto;

public record RequestsForMyRideDTO(
		Long id_solicitacao,
		String nome_passageiro,
		String foto,
		String curso,
		OriginDTO originDTO,
		DestinationDTO destinationDTO,
		Long id_carona,
		String status		
) {
}
