package com.backendFateCarona.dto;

import com.backendFateCarona.entity.User;

public record VehicleDTO(
	    User usuario,
	    String modelo,
	    String marca,
	    String placa,
	    String cor,
	    Integer ano
) {}
