package aceitacao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

class testAceitacaoCriarUsuario {

	@Test
	public void testAceitacaoCriarUsuario() {
	    String json = """
	    {
	        "nome": "UsuarioTesteNovo",
	        "sobrenome": "Silva",
	        "senha": "senha123",
	         "email": "usuario.teste.novo@example.com",
	        "telefone": "123456789",
	        "foto": "url_foto",
	        "tipoUsuario": "PASSAGEIRO",
	        "genero": "MASCULINO"
	    }
	    """;

	    given()
	        .contentType("application/json")
	        .body(json)
	    .when()
	        .post("http://localhost:8080/users/criarUsuario")
	    .then()
	        .statusCode(201)
	        .body("email", equalTo("usuario.teste.novo@example.com"))
	        .body("nome", equalTo("UsuarioTesteNovo"));
	}


}
