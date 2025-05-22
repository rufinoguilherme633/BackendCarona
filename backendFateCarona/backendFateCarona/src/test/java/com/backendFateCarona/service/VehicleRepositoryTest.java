package com.backendFateCarona.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.backendFateCarona.entity.Gender;
import com.backendFateCarona.entity.User;
import com.backendFateCarona.entity.UserType;
import com.backendFateCarona.entity.Vehicle;
import com.backendFateCarona.repository.UserRepository;
import com.backendFateCarona.repository.VehiceRepositore;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
@DataJpaTest
class VehicleRepositoryTest {

	 @Autowired
	    private VehiceRepositore vehicleRepository;

	    @Autowired
	    private UserRepository userRepository;

	    @Test
	    public void testFindByPlaca() {
	        // Cria e salva um usuário (relacionamento obrigatório)
	        User user = new User();
	        user.setNome("João");
	        user.setSobrenome("Silva");
	        user.setEmail("joao@teste.com");
	        user.setSenha("senha123");
	        user.setTelefone("11999998888");
	        user.setFoto("foto.png");
	        user.setTipoUsuario(UserType.MOTORISTA);
	        user.setGenero(Gender.MASCULINO);

	        user = userRepository.save(user);

	        // Cria e salva um veículo
	        Vehicle vehicle = new Vehicle();
	        vehicle.setUsuario(user);
	        vehicle.setMarca("Honda");
	        vehicle.setModelo("Civic");
	        vehicle.setPlaca("ABC1234");
	        vehicle.setCor("Preto");
	        vehicle.setAno(2020);

	        vehicleRepository.save(vehicle);

	        // Testa o método findByPlaca
	        Optional<Vehicle> result = vehicleRepository.findByPlaca("ABC1234");

	        assertThat(result).isPresent();
	        assertThat(result.get().getMarca()).isEqualTo("Honda");
	        assertThat(result.get().getUsuario().getEmail()).isEqualTo("joao@teste.com");
	    }
}
