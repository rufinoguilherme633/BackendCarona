package com.backendFateCarona.integracao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.backendFateCarona.dto.UserDTO;
import com.backendFateCarona.entity.Gender;
import com.backendFateCarona.entity.UserType;
import com.backendFateCarona.repository.UserRepository;
import com.backendFateCarona.service.UserService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void deveCriarUsuarioComSucesso() throws Exception {
        UserDTO userDTO = new UserDTO(
            "João",
            "Silva",
            "senha123",

            "joao.silva@teste.com",
            "123456789",
            "url_foto",
            UserType.PASSAGEIRO,
            Gender.MASCULINO
        );

        // Execução
        UserDTO usuarioCriado = userService.createUser(userDTO);

        // Verificação
        assertThat(usuarioCriado).isNotNull();
        assertThat(usuarioCriado.email()).isEqualTo("joao.silva@teste.com");

        // Verifica se o usuário foi salvo no banco
        assertThat(userRepository.findByEmail("joao.silva@teste.com")).isPresent();
    }

    @Test
    public void deveLancarExcecaoParaEmailDuplicado() {
        UserDTO primeiroUsuario = new UserDTO(
            "Maria",
            "Oliveira",
           
            "senha123",
            "maria.oliveira@teste.com",
            "11999997777",
            "foto.jpg",
            UserType.PASSAGEIRO,
            Gender.FEMININO
        );

        try {
            userService.createUser(primeiroUsuario);
        } catch (Exception e) {
            fail("Não deveria lançar exceção aqui");
        }

        UserDTO usuarioDuplicado = new UserDTO(
            "Maria",
            "Silva",
            
            "outrasenha",
            "maria.oliveira@teste.com", // Email duplicado
            "11999996666"
            ,
            "outrafoto.jpg",
            UserType.PASSAGEIRO,
            Gender.FEMININO
        );

        // Execução e Verificação
        Exception excecao = assertThrows(Exception.class, () -> {
            userService.createUser(usuarioDuplicado);
        });

        assertThat(excecao.getMessage()).isEqualTo("Email já cadastrado em nosso sistema");
    }
}
