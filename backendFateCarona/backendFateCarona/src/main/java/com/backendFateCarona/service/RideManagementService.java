package com.backendFateCarona.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.backendFateCarona.dto.AdressDTO;
import com.backendFateCarona.dto.RideManagementDTO;
import com.backendFateCarona.dto.RideManagementRequestDTO;
import com.backendFateCarona.dto.RideManagementRequestUpdateDTO;
import com.backendFateCarona.entity.RideManagement;
import com.backendFateCarona.entity.RideStatus;
import com.backendFateCarona.entity.User;
import com.backendFateCarona.entity.UserType;
import com.backendFateCarona.repository.RideManagementRepository;
import com.backendFateCarona.repository.UserRepository;

@Service
public class RideManagementService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	RideManagementRepository rideManagementRepository;
	
	RideManagement rideManagement;
	@Autowired
	OpenstreetmapService openstreetmapService;
	
	
	public void validateUserIsDriver(User user) throws Exception {
		if(user.getTipoUsuario() == UserType.PASSAGEIRO) {
			throw new Exception("Usuarios do tipo passgeiros não podem criar caronas");
		}
	}

	public User validateUserExist(Long id) throws Exception {
		Optional<User> user = userRepository.findById(id);
		if(user.isEmpty()) {
			throw new Exception("Motorista inexistente");
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
	
	
	public RideManagementDTO createRide(RideManagementRequestDTO ride) throws Exception {
		
		User user = validateUserExist(ride.motorista());
		validateUserIsDriver(user);
		AdressDTO origem = validateLocal(ride.origem());
		AdressDTO destino = validateLocal(ride.destino());
		validateData(ride.dataHora());
		
		 RideManagement newRide = new RideManagement();
		    newRide.setMotorista(user);
		    newRide.setOrigem(ride.origem());
		    newRide.setLongitudeOrigem(Double.parseDouble(origem.lon()));
		    newRide.setLatitudeOrigem(Double.parseDouble(origem.lat()));
		    newRide.setDestino(ride.destino());
		    newRide.setLongitudeDestino(Double.parseDouble(destino.lon()));
		    newRide.setLatitudeDestino(Double.parseDouble(destino.lat()));
		    newRide.setDataHora(ride.dataHora());
		    newRide.setVagasDisponiveis(ride.vagasDisponiveis());
		    newRide.setStatus(RideStatus.ATIVA);
		    

		    newRide = rideManagementRepository.save(newRide);
		    return new RideManagementDTO(
		            newRide.getMotorista().getIdUsuario(),
		            newRide.getOrigem(),
		            newRide.getLongitudeOrigem(),
		            newRide.getLatitudeOrigem(),
		            newRide.getDestino(),
		            newRide.getLongitudeDestino(),
		            newRide.getLatitudeDestino(),
		            newRide.getDataHora(),
		            newRide.getVagasDisponiveis(),
		            newRide.getStatus()
		        );
		    	    
	}
	public RideManagementDTO updateRide(Long id_carona,  RideManagementRequestUpdateDTO ride) throws Exception{
		
		User user = validateUserExist(ride.motorista());
		validateUserIsDriver(user);
		AdressDTO origem = validateLocal(ride.origem());
		AdressDTO destino = validateLocal(ride.destino());
		validateData(ride.dataHora());
		RideManagement existingRide = validaRideExist(id_carona);
		
		existingRide.setOrigem(ride.origem());
		existingRide.setLatitudeOrigem(Double.parseDouble(origem.lat()));
		existingRide.setLongitudeOrigem(Double.parseDouble(origem.lon()));
		existingRide.setDestino(ride.destino());
		existingRide.setLatitudeDestino(Double.parseDouble(destino.lat()));
		existingRide.setLongitudeDestino(Double.parseDouble(destino.lon()));		    
		existingRide.setDataHora(ride.dataHora());
		existingRide.setVagasDisponiveis(ride.vagasDisponiveis());
		existingRide.setStatus(ride.status());
		
		RideManagement ridePpdate=  rideManagementRepository.save(existingRide);
		
		return new RideManagementDTO(
				ridePpdate.getMotorista().getIdUsuario(),
	            ridePpdate.getOrigem(),
	            ridePpdate.getLongitudeOrigem(),
	            ridePpdate.getLatitudeOrigem(),
	            ridePpdate.getDestino(),
	            ridePpdate.getLongitudeDestino(),
	            ridePpdate.getLatitudeDestino(),
	            ridePpdate.getDataHora(),
	            ridePpdate.getVagasDisponiveis(),
	            ridePpdate.getStatus()
	        );
	}
	
	public void deleteRide(Long id_carona) {
		RideManagement existingRide = validaRideExist(id_carona);
		rideManagementRepository.deleteById(id_carona);
	}
	
	public List<RideManagementRequestUpdateDTO> getAllRides(Long motoristaId) throws Exception {
	    User user = validateUserExist(motoristaId);
	    List<RideManagement> rides = rideManagementRepository.findAllByMotorista(user);

	    List<RideManagementRequestUpdateDTO> dtoList = rides.stream()
	        .map(ride -> new RideManagementRequestUpdateDTO(
	           
	            ride.getMotorista().getIdUsuario(),  // pega o ID do motorista
	            ride.getOrigem(),
	            ride.getDestino(),
	            ride.getDataHora(),
	            ride.getVagasDisponiveis(),
	            ride.getStatus()
	        ))
	        .toList();

	    return dtoList;
	}

	
	
}
