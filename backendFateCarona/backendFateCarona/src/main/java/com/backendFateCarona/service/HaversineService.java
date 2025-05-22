package com.backendFateCarona.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.backendFateCarona.dto.RouteCoordinatesDTO;
import com.backendFateCarona.entity.RideManagement;
import com.backendFateCarona.repository.RideManagementRepository;


@Service
public class HaversineService {
	
	private static final double RAIO_TERRA = 6371; 
   	private static final double PI = Math.PI;
   	@Autowired
   	RideManagementRepository rideRepository;
   	
   	
	public List<RideManagement> findNearbyDrivers( RouteCoordinatesDTO routeCoordinatesDTO) throws Exception{
		
		
		List<RideManagement> rideRepositore = rideRepository.findAll();
		
        List<RideManagement> motoristaProximos =new ArrayList<>();
        
        for(RideManagement i : rideRepositore) {
        	
        	double distanciaOrigem = this.calcularDistancia(routeCoordinatesDTO.latitudeOrigem(), routeCoordinatesDTO.longitudeOrigem(),i.getLatitudeOrigem(), i.getLongitudeOrigem());
        	double distanciaDestino = this.calcularDistancia(routeCoordinatesDTO.latitudeDestino(), routeCoordinatesDTO.longitudeDestino(),i.getLatitudeDestino(), i.getLongitudeDestino());
        	if(distanciaOrigem < 9.000000  &&  distanciaDestino < 0.3000000000000000  ) {
				motoristaProximos.add(i);
			}
        }
        
        
        if(motoristaProximos.isEmpty()) {
        	throw new Exception("Nenhum motorista proximo");
        }
        
        return motoristaProximos;
	}
   	
	public  double calcularDistancia( double latitude1,double longitude1,double latitude2,double longitude2){
	   latitude1 = transformarEmRadianos(latitude1);
       longitude1 = transformarEmRadianos(longitude1);
       latitude2 = transformarEmRadianos(latitude2);
       longitude2 = transformarEmRadianos(longitude2);
	    
	    double diferencaLatitude = calcularDiferencaValores( latitude1 , latitude2);
	    double diferencaLongitude = calcularDiferencaValores(longitude1 , longitude2);

	    double valorSenoLatitudeDividido2 = elevarAoQuadrado(transformarSeno(diferencaLatitude/2));


	    double valorSenoLongitudeDividido2 = elevarAoQuadrado(transformarSeno(diferencaLongitude/2));
	    
	    double cossenoLatitude1 = transformarCosseno(latitude1);
	    double cossenoLatitude2 = transformarCosseno(latitude2);
	    double a  = Math.asin(Math.sqrt(valorSenoLatitudeDividido2 + cossenoLatitude1 * cossenoLatitude2 * valorSenoLongitudeDividido2 ));
	    double resultado = 2 * RAIO_TERRA * a ;
	    return  resultado;

	}
	
	public  double elevarAoQuadrado(double valor){
	    return Math.pow(valor,2);
	}
	
	public static double transformarSeno(double valor){
	    return  Math.sin(valor);
	}

	public  double transformarCosseno(double valor){
	    return Math.cos(valor);
	}
	public  double transformarEmRadianos(double valor){
	    double resultado = valor * (PI / 180);
	    return resultado;
	}
	
	public double calcularDiferencaValores(double valor1,double  valor2) {
		return valor2 - valor1;
	}
}
