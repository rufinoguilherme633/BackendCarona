package com.backendFateCarona.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.backendFateCarona.dto.DriverDTO;
import com.backendFateCarona.dto.UserDTO;
import com.backendFateCarona.dto.VehicleDTO;
import com.backendFateCarona.entity.User;
import com.backendFateCarona.entity.Vehicle;
import com.backendFateCarona.repository.UserRepository;

@Service
public class UserService {

	@Autowired 
	UserRepository userRepository;
	@Autowired
	VehiceService vehiceService;
	
	
	public void existsByEmail(String email) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        
        if(user.isPresent()) {
        	throw  new Exception("Email já cadastrado em nosso sistema");
        }
	}
	
	
	public void existsByUserById(long id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        
        if(!user.isPresent()) {
        	throw  new Exception("Usuario não encontrado");
        }
	}
	
	
	public void validateEmailUpdate(Long id, String email) throws Exception {
        Optional<User> existingUser = userRepository.findByEmail(email);
		if(existingUser.isPresent() && !existingUser.get().getIdUsuario().equals(id)){
			throw  new Exception("Email já cadastrado");
		}
	}
	
	public UserDTO createUser(UserDTO userDTO) throws Exception {
		existsByEmail(userDTO.email());
		User newUser = new User();
        newUser.setNome(userDTO.nome());
        newUser.setSobrenome(userDTO.sobrenome());
        newUser.setEmail(userDTO.email());
        newUser.setSenha(userDTO.senha());
        newUser.setTelefone(userDTO.telefone());
        newUser.setFoto(userDTO.foto());
        newUser.setGenero(userDTO.genero());
        newUser.setTipoUsuario(userDTO.tipoUsuario());;
		this.userRepository.save(newUser);
		return userDTO;
	
	}
	
	 
	    public UserDTO updateUser(Long id , UserDTO userDTO) throws Exception {
	    	existsByUserById(id);
	    	validateEmailUpdate(id, userDTO.email());
	    	
	        Optional<User> userOptional = userRepository.findById(id);
	        User existingUser = userOptional.get();
	        
	        existingUser.setNome(userDTO.nome());
	        existingUser.setSobrenome(userDTO.sobrenome());
	        existingUser.setEmail(userDTO.email());
	        existingUser.setSenha(userDTO.senha());
	        existingUser.setTelefone(userDTO.telefone());
	        existingUser.setFoto(userDTO.foto());
	        existingUser.setTipoUsuario(userDTO.tipoUsuario());
	        existingUser.setGenero(userDTO.genero());
	        this.userRepository.save(existingUser);
	        
	    	return userDTO;
		 
	 }
	
	public DriverDTO createDriver(DriverDTO driverDTO) throws Exception {
		existsByEmail(driverDTO.email());
	    User newUser = new User();
	    newUser.setNome(driverDTO.nome());
	    newUser.setSobrenome(driverDTO.sobrenome());
	    newUser.setEmail(driverDTO.email());
	    newUser.setSenha(driverDTO.senha());
	    newUser.setTelefone(driverDTO.telefone());
	    newUser.setFoto(driverDTO.foto());
	    newUser.setGenero(driverDTO.genero());
	    newUser.setTipoUsuario(driverDTO.tipoUsuario());

	    this.userRepository.save(newUser);

	    VehicleDTO vehicleDTO = new VehicleDTO(
	        newUser,
	        driverDTO.vehicle().getModelo(),
	        driverDTO.vehicle().getMarca(),
	        driverDTO.vehicle().getPlaca(),
	        driverDTO.vehicle().getCor(),
	        driverDTO.vehicle().getAno()
	    );

	    this.vehiceService.createVehice(vehicleDTO);

	    return driverDTO;
	}
		
	
	
    public DriverDTO updateDriver(Long id, DriverDTO driverDTO) throws Exception {
    	existsByUserById(id);
    	validateEmailUpdate(id, driverDTO.email());
    	
        Optional<User> userOptional = userRepository.findById(id);
        User existingUser = userOptional.get();
        
        existingUser.setNome(driverDTO.nome());
        existingUser.setSobrenome(driverDTO.sobrenome());
        existingUser.setEmail(driverDTO.email());
        existingUser.setSenha(driverDTO.senha());
        existingUser.setTelefone(driverDTO.telefone());
        existingUser.setFoto(driverDTO.foto());
        existingUser.setTipoUsuario(driverDTO.tipoUsuario());
        existingUser.setGenero(driverDTO.genero());
        
       // -------- Acrescentar Logica atualizar carro e motorista------
        this.userRepository.save(existingUser);
        Vehicle updatedVehicle = vehiceService.updateVehicle(existingUser, driverDTO.vehicle());

    	return driverDTO;
	 
 }
}
