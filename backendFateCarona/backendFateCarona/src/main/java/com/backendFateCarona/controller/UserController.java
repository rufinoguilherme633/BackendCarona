package com.backendFateCarona.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backendFateCarona.dto.DriverDTO;
import com.backendFateCarona.dto.UserDTO;
import com.backendFateCarona.entity.User;
import com.backendFateCarona.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping(value = "/criarUsuario",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) throws Exception{
		UserDTO newUser = userService.createUser(userDTO);
		return new ResponseEntity<>(newUser,HttpStatus.CREATED);
	}
	
	@PostMapping("/criarMotorista")
	public ResponseEntity<DriverDTO> createDriver(@RequestBody DriverDTO driverDTO) throws Exception{
		DriverDTO driver = userService.createDriver(driverDTO);
		return new ResponseEntity<>(driver,HttpStatus.CREATED);
	}
	
	 @PutMapping("/passageiro/{id}")
	    public ResponseEntity<?> updateUser(@PathVariable(name = "id") Long id, @RequestBody UserDTO userDTO) throws Exception {
		 UserDTO newUser = userService.updateUser(id, userDTO);
		 return new ResponseEntity<>(newUser,HttpStatus.OK);
	 }
	 
	 
	 @PutMapping("/motorista/{id}")
	    public ResponseEntity<?> motoristaUser(@PathVariable(name = "id") Long id, @RequestBody DriverDTO driverDTO) throws Exception {
		 DriverDTO driverDTO2 = userService.updateDriver(id, driverDTO);
		 return new ResponseEntity<>(driverDTO2,HttpStatus.OK);
	 }
}
