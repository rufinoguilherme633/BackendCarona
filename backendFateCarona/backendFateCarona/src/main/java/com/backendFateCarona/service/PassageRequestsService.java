package com.backendFateCarona.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backendFateCarona.dto.AdressDTO;
import com.backendFateCarona.dto.PassageRequestsDTO;
import com.backendFateCarona.dto.PassageRequestsRequestsDTO;
import com.backendFateCarona.dto.PassageRequestsUpdateDTO;
import com.backendFateCarona.dto.RideManagementDTO;
import com.backendFateCarona.dto.RideManagementRequestUpdateDTO;
import com.backendFateCarona.entity.PassageRequests;
import com.backendFateCarona.entity.RequestStatus;
import com.backendFateCarona.entity.RideManagement;
import com.backendFateCarona.entity.RideStatus;
import com.backendFateCarona.entity.User;
import com.backendFateCarona.repository.PassageRequestsRepository;
import com.backendFateCarona.repository.RideManagementRepository;
import com.backendFateCarona.repository.UserRepository;
@Service
public class PassageRequestsService {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	OpenstreetmapService openstreetmapService;
	
	@Autowired
	RideManagementRepository rideManagementRepository;
	
	@Autowired
	PassageRequestsRepository passageRequestsRepository;

	public User validateUserExist(Long id) throws Exception {
		Optional<User> user = userRepository.findById(id);
		if(user.isEmpty()) {
			throw new Exception("Passageiro inexistente");
			
		}
		
		return user.get();
	}
	
	
	public AdressDTO validateLocal(String local) {
		Optional<AdressDTO> address =  openstreetmapService.buscarLocal(local);
		if(address.isEmpty()) {
	    	  throw new IllegalArgumentException("Local não encontrado");

	    }
		
		return address.get();
	}
	public void validateData(LocalDateTime data) {
		if (data.isBefore(LocalDateTime.now()) ) {
	        throw new IllegalArgumentException("Data e hora da carona não podem ser anteriores ao momento atual");
		}
	}
	
	
	public RideManagement validaRideExist(Long id_carona) {
		Optional<RideManagement> ride =  rideManagementRepository.findById(id_carona);
		if(ride.isEmpty()) {
	    	  throw new IllegalArgumentException("Carona  não encontrado");

	    }
		
		return ride.get();
	}
	
	public PassageRequests validaPassageRequestExist(Long id_solitacao) {
		Optional<PassageRequests> passage =  passageRequestsRepository.findById(id_solitacao);
		if(passage.isEmpty()) {
	    	  throw new IllegalArgumentException("Solitação não existe");

	    }
		
		return passage.get();
	}
	
	public PassageRequestsDTO createPassageRequests(PassageRequestsRequestsDTO passageRequestsService) throws Exception {
		User user = validateUserExist(passageRequestsService.passageiro());
		AdressDTO origem = validateLocal(passageRequestsService.origem());
		AdressDTO destino = validateLocal(passageRequestsService.destino());
		validateData(passageRequestsService.dataHora());
		RideManagement rideManagement= validaRideExist(passageRequestsService.carona());
		
		PassageRequests newPassageRequestsService = new PassageRequests();
		newPassageRequestsService.setCarona(rideManagement);
	    newPassageRequestsService.setPassageiro(user);;
	    newPassageRequestsService.setOrigem(passageRequestsService.origem());
	    newPassageRequestsService.setLongitudeOrigem(Double.parseDouble(origem.lon()));
	    newPassageRequestsService.setLatitudeOrigem(Double.parseDouble(origem.lat()));
	    newPassageRequestsService.setDestino(passageRequestsService.destino());
	    newPassageRequestsService.setLongitudeDestino(Double.parseDouble(destino.lon()));
	    newPassageRequestsService.setLatitudeDestino(Double.parseDouble(destino.lat()));
	    newPassageRequestsService.setDataHora(passageRequestsService.dataHora());
	    newPassageRequestsService.setStatus(RequestStatus.PENDENTE);
	    

	    newPassageRequestsService = passageRequestsRepository.save(newPassageRequestsService);
	    return new PassageRequestsDTO(
	    	    newPassageRequestsService.getCarona().getIdCarona(),                 // carona
	    	    newPassageRequestsService.getPassageiro().getIdUsuario(),      // passageiro
	    	    newPassageRequestsService.getDataHora(),                       // dataHora
	    	    newPassageRequestsService.getOrigem(),                         // origem
	    	    newPassageRequestsService.getLongitudeOrigem(),                // longitudeOrigem
	    	    newPassageRequestsService.getLatitudeOrigem(),                 // latitudeOrigem
	    	    newPassageRequestsService.getDestino(),                        // destino
	    	    newPassageRequestsService.getLongitudeDestino(),               // longitudeDestino
	    	    newPassageRequestsService.getLatitudeDestino(),                // latitudeDestino
	    	    newPassageRequestsService.getStatus()                          // status
	    	);

	}
	
	
	public PassageRequestsDTO updatePassageRequests(Long id_Solicitacao, PassageRequestsUpdateDTO passageRequestsService) throws Exception {
		
		User user = validateUserExist(passageRequestsService.passageiro());
		AdressDTO origem = validateLocal(passageRequestsService.origem());
		AdressDTO destino = validateLocal(passageRequestsService.destino());
		validateData(passageRequestsService.dataHora());
		RideManagement rideManagement= validaRideExist(passageRequestsService.carona());
		PassageRequests existPassageRequests = validaPassageRequestExist(id_Solicitacao);
		
		existPassageRequests.setCarona(rideManagement);
	    existPassageRequests.setPassageiro(user);;
	    existPassageRequests.setOrigem(passageRequestsService.origem());
	    existPassageRequests.setLongitudeOrigem(Double.parseDouble(origem.lon()));
	    existPassageRequests.setLatitudeOrigem(Double.parseDouble(origem.lat()));
	    existPassageRequests.setDestino(passageRequestsService.destino());
	    existPassageRequests.setLongitudeDestino(Double.parseDouble(destino.lon()));
	    existPassageRequests.setLatitudeDestino(Double.parseDouble(destino.lat()));
	    existPassageRequests.setDataHora(passageRequestsService.dataHora());
	    existPassageRequests.setStatus(passageRequestsService.status());
	    

	    existPassageRequests = passageRequestsRepository.save(existPassageRequests);
	    return new PassageRequestsDTO(
	    	    existPassageRequests.getCarona().getIdCarona(),                 // carona
	    	    existPassageRequests.getPassageiro().getIdUsuario(),      // passageiro
	    	    existPassageRequests.getDataHora(),                       // dataHora
	    	    existPassageRequests.getOrigem(),                         // origem
	    	    existPassageRequests.getLongitudeOrigem(),                // longitudeOrigem
	    	    existPassageRequests.getLatitudeOrigem(),                 // latitudeOrigem
	    	    existPassageRequests.getDestino(),                        // destino
	    	    existPassageRequests.getLongitudeDestino(),               // longitudeDestino
	    	    existPassageRequests.getLatitudeDestino(),                // latitudeDestino
	    	    existPassageRequests.getStatus()                          // status
	    	);

	}
	
	public void deletePassageRequests(Long id_solicitacao) {
		PassageRequests existPassageRequests = validaPassageRequestExist(id_solicitacao);
		passageRequestsRepository.deleteById(id_solicitacao);
	}

	
	public List<PassageRequestsUpdateDTO> getAllRequests(Long id_passageiro) throws Exception {
	    User user = validateUserExist(id_passageiro);
	    List<PassageRequests> passageiros = passageRequestsRepository.findAllByPassageiro(user);

	    List<PassageRequestsUpdateDTO> dtoList = passageiros.stream()
	        .map(passageiro -> new PassageRequestsUpdateDTO(
	        	passageiro.getCarona().getIdCarona(),
	        	passageiro.getPassageiro().getIdUsuario(),  // pega o ID do motorista
	        	passageiro.getDataHora(),
	        	passageiro.getOrigem(),
	        	passageiro.getDestino(),
	        	passageiro.getStatus()
	        ))
	        .toList();

	    return dtoList;
	}
}
