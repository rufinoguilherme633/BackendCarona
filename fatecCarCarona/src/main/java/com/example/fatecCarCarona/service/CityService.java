package com.example.fatecCarCarona.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fatecCarCarona.dto.CityDTO;
import com.example.fatecCarCarona.entity.City;
import com.example.fatecCarCarona.entity.Course;
import com.example.fatecCarCarona.entity.State;
import com.example.fatecCarCarona.repository.CityRepository;

@Service
public class CityService {

	@Autowired
	CityRepository cityRepository;
	@Autowired
	StateService stateService;
	
	public City validateCity(long id) {
		return cityRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Cidade não cadastrada no nosso sistema"));
}

	public City validateCity(String nome) {
		return cityRepository.findByNome(nome)
				.orElseThrow(() -> new RuntimeException("Cidade não cadastrada no nosso sistema"));
}

		public List<CityDTO> allCitiessByStateId(Long stateId) throws Exception{
			Optional<State> stateExists = stateService.validateStateExists(stateId);
			List<City> allCities = cityRepository.findByStateId(stateExists.get().getId());
			
			if(allCities.isEmpty()) {
				throw new Exception("Não ha cidades cadastradas");
			}
			  List<CityDTO> cityDTOs = allCities.stream()
				        .map(city -> new CityDTO(city.getId(),city.getNome(),city.getEstado().getId(),city.getIbge()))
				        .toList();

				    return cityDTOs;
		}

}
