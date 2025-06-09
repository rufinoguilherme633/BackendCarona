	package com.example.fatecCarCarona.service;

	import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.fatecCarCarona.dto.ViaCepDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

	@Service
	public class ViaCepService {


		private final String baseUrl = "https://viacep.com.br/ws/";

		public Optional<ViaCepDTO> buscarCep(String cep){

		try {
			String urlString = baseUrl+ cep + "/json";
			URL url = new URL(urlString);

			HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
	        conexao.setRequestMethod("GET");
	        if (conexao.getResponseCode() != 200) {
	        	return Optional.empty();
	        }


	        BufferedReader br = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            StringBuilder retorno = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                retorno.append(line);
            }

            br.close();

            ObjectMapper objectMapper = new ObjectMapper();
            ViaCepDTO resultado = objectMapper.readValue(retorno.toString(), ViaCepDTO.class);

            return Optional.ofNullable(resultado);

        } catch (Exception e) {
        	throw new RuntimeException("Erro ao buscar endereço: " + e.getMessage(), e);
        }
    }


	}
