package com.backendFateCarona.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backendFateCarona.dto.VehicleDTO;
import com.backendFateCarona.entity.User;
import com.backendFateCarona.entity.Vehicle;
import com.backendFateCarona.repository.VehiceRepositore;

@Service
public class VehiceService {
	@Autowired 
	VehiceRepositore vehiceRepositore; 
	
	public void existsByPlaca(String placa) throws Exception {
		Optional<Vehicle> vehicle = vehiceRepositore.findByPlaca(placa);
		if(vehicle.isPresent()) {
			throw new Exception("Palca já cadastrada em nosso sistema");
		}
	}
	
	
	public void saveVehice(VehicleDTO vehicle) throws Exception {
		existsByPlaca(vehicle.placa());		
		Vehicle newVehicle = new Vehicle();
	    newVehicle.setUsuario(vehicle.usuario());
	    newVehicle.setModelo(vehicle.modelo());
	    newVehicle.setMarca(vehicle.marca());
	    newVehicle.setPlaca(vehicle.placa());
	    newVehicle.setCor(vehicle.cor());
	    newVehicle.setAno(vehicle.ano());
		vehiceRepositore.save(newVehicle);
	}
	
	public VehicleDTO createVehice(VehicleDTO vehicle) throws Exception {
		 
		 this.saveVehice(vehicle);
		 return vehicle;
	}
	
	
	public Vehicle updateVehicle(User user, Vehicle vehicle) {
	    Optional<Vehicle> userVehicle = vehiceRepositore.findByUsuario(user);
	    Vehicle vehicleToSave = userVehicle.orElse(new Vehicle());
	    vehicleToSave.setModelo(vehicle.getModelo());
	    vehicleToSave.setMarca(vehicle.getMarca());
	    vehicleToSave.setPlaca(vehicle.getPlaca());
	    vehicleToSave.setCor(vehicle.getCor());
	    vehicleToSave.setAno(vehicle.getAno());
	    vehicleToSave.setUsuario(user);
	    return vehiceRepositore.save(vehicleToSave);
	}

}
