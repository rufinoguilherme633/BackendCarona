package com.backendFateCarona.dto;

import com.backendFateCarona.entity.Gender;
import com.backendFateCarona.entity.UserType;
import com.backendFateCarona.entity.Vehicle;

public record DriverDTO(
		   String nome,
		   String sobrenome,
		    String email,
		    String senha,
		    String telefone,
		    String foto,
		    UserType tipoUsuario,
		    Gender genero,
		    Vehicle vehicle
		) {}
