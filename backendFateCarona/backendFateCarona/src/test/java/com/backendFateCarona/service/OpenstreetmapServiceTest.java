package com.backendFateCarona.service;

import com.backendFateCarona.dto.AdressDTO;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OpenstreetmapServiceTest {

    private final OpenstreetmapService service = new OpenstreetmapService();

    @Test
    public void testBuscarLocal_ComEnderecoValido_DeveRetornarCoordenadas() {
        // Arrange
        String endereco = "Rua cuiaba cotia";

        // Act
        Optional<AdressDTO> resultado = service.buscarLocal(endereco);

        // Assert
        assertTrue(resultado.isPresent(), "Deveria retornar um resultado para o endereço válido");

        AdressDTO enderecoEncontrado = resultado.get();
        System.out.println("Latitude: " + enderecoEncontrado.lat());
        System.out.println("Longitude: " + enderecoEncontrado.lon());

        assertNotNull(enderecoEncontrado.lat(), "Latitude não deve ser nula");
        assertNotNull(enderecoEncontrado.lon(), "Longitude não deve ser nula");
    }

    @Test
    public void testBuscarLocal_ComEnderecoInvalido_DeveRetornarOptionalVazio() {
        // Arrange
        String endereco = "asdfghjklqwertyuiop sem sentido";

        // Act
        Optional<AdressDTO> resultado = service.buscarLocal(endereco);

        // Assert
        assertFalse(resultado.isPresent(), "Deveria retornar vazio para endereço inválido");
    }
}
