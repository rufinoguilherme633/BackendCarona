package aceitacao;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
class testAceitacaoCriarMotorista {

	 @Test
	    public void testAceitacaoCriarMotorista() {
	        String json = """
	        {
	            "nome": "Joao",
	            "sobrenome": "Silveira",
	            "email": "joao.silveira@exemplo.com",
	            "senha": "senhaSegura123",
	            "telefone": "11999998888",
	            "foto": "url_da_foto",
	            "tipoUsuario": "MOTORISTA",
	            "genero": "MASCULINO",
	            "vehicle": {
	                "modelo": "Civic",
	                "marca": "Honda",
	                "placa": "XYZ1234",
	                "cor": "Preto",
	                "ano": 2020
	            }
	        }
	        """;

	        given()
	            .contentType("application/json")
	            .body(json)
	        .when()
	            .post("http://localhost:8080/users/criarMotorista")
	        .then()
	            .statusCode(201)
	            .body("email", equalTo("joao.silveira@exemplo.com"))
	            .body("nome", equalTo("Joao"))
	            .body("vehicle.modelo", equalTo("Civic"));
	    }

}
