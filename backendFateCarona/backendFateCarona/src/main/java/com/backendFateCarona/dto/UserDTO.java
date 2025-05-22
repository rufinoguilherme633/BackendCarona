package com.backendFateCarona.dto;

import com.backendFateCarona.entity.Gender;
import com.backendFateCarona.entity.UserType;

public record UserDTO(
    
    String nome,
    String sobrenome,
    String senha,
    String email,
    String telefone,
    String foto,
    UserType tipoUsuario,
    Gender genero
) {}
