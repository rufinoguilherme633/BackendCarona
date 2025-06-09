package com.example.fatecCarCarona.service;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.fatecCarCarona.dto.DestinationDTO;
import com.example.fatecCarCarona.dto.DestinationResponseDTO;
import com.example.fatecCarCarona.dto.OpenstreetmapDTO;
import com.example.fatecCarCarona.dto.OriginDTO;
import com.example.fatecCarCarona.dto.OriginResponseDTO;
import com.example.fatecCarCarona.dto.RideDTO;
import com.example.fatecCarCarona.dto.RideResponseDTO;
import com.example.fatecCarCarona.dto.VehicleResponseDTO;
import com.example.fatecCarCarona.dto.ViaCepDTO;
import com.example.fatecCarCarona.entity.City;
import com.example.fatecCarCarona.entity.Destination;
import com.example.fatecCarCarona.entity.Origin;
import com.example.fatecCarCarona.entity.Ride;
import com.example.fatecCarCarona.entity.RideStatus;
import com.example.fatecCarCarona.entity.User;
import com.example.fatecCarCarona.entity.Vehicle;
import com.example.fatecCarCarona.exception.RideException;
import com.example.fatecCarCarona.repository.RideRepository;
import com.example.fatecCarCarona.repository.RideStatusRepository;
import com.example.fatecCarCarona.repository.UserRepository;
import com.example.fatecCarCarona.repository.VehicleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RideService {

	private final RideRepository rideRepository;
	private final UserRepository userRepository;
	private final VehicleRepository vehicleRepository;
	private final RideStatusRepository rideStatusRepository;

	private final ViaCepService viaCepService;
	private final CityService cityService;
	private final VehicleService vehicleService;
	private final OriginService originService;
	private final DestinationService destinationService;
	private final OpenstreetmapService openstreetmapService;
	private final RideStatusService rideStatusService;

	private void validateAddress(String cep, String cidade, String logradouro, String bairro) {
		Optional<ViaCepDTO> viaCepDTO = viaCepService.buscarCep(cep);

		if (viaCepDTO.isEmpty()) {
			throw new RideException("CEP não encontrado: " + cep);
		}

		boolean isValid = viaCepDTO.get().localidade().equals(cidade) &&
						 viaCepDTO.get().logradouro().equals(logradouro) &&
						 viaCepDTO.get().bairro().equals(bairro);

		if (!isValid) {
			throw new RideException("Endereço não corresponde ao CEP informado");
		}
	}

	private Optional<OpenstreetmapDTO> buscarLocalizacao(String endereco) {
		String enderecoEncoded = URLEncoder.encode(endereco, StandardCharsets.UTF_8);
		Optional<OpenstreetmapDTO> resultado = openstreetmapService.buscarLocal(enderecoEncoded);

		if (resultado.isEmpty()) {
			throw new RideException("Endereço não encontrado no OpenStreetMap: " + endereco);
		}

		return resultado;
	}

	private Origin criarOrigem(OriginDTO originDTO, City cidade, OpenstreetmapDTO localizacao) {
		Origin origem = new Origin();
		origem.setCity(cidade);
		origem.setLogradouro(originDTO.logradouro());
		origem.setNumero(originDTO.numero());
		origem.setBairro(originDTO.bairro());
		origem.setCep(originDTO.cep());
		origem.setLatitude(Double.parseDouble(localizacao.lat()));
		origem.setLongitude(Double.parseDouble(localizacao.lon()));
		return origem;
	}

	private Destination criarDestino(DestinationDTO destinationDTO, City cidade, OpenstreetmapDTO localizacao) {
		Destination destino = new Destination();
		destino.setCity(cidade);
		destino.setLogradouro(destinationDTO.logradouro());
		destino.setNumero(destinationDTO.numero());
		destino.setBairro(destinationDTO.bairro());
		destino.setCep(destinationDTO.cep());
		destino.setLatitude(Double.parseDouble(localizacao.lat()));
		destino.setLongitude(Double.parseDouble(localizacao.lon()));
		return destino;
	}

	private void validarMotoristaDisponivel(Long motoristaid) {
		List<Ride> corridasAtivas = rideRepository.findAtivasByDriverId(motoristaid);
		if (!corridasAtivas.isEmpty()) {
			throw new RideException("Motorista já possui uma corrida ativa. Finalize ou cancele a corrida atual.");
		}
	}

	@Transactional
	public RideDTO criarCarona(Long motoristaid, RideDTO dto) throws Exception {
		User motorista = userRepository.findById(motoristaid)
			.orElseThrow(() -> new RideException("Motorista não encontrado"));

		validarMotoristaDisponivel(motoristaid);
		vehicleService.validateUserIsVehicleOwner(motoristaid, dto.id_veiculo());

		Vehicle veiculo = vehicleRepository.findById(dto.id_veiculo())
			.orElseThrow(() -> new RideException("Veículo não encontrado"));

		validateAddress(dto.originDTO().cep(), dto.originDTO().cidade(),
					   dto.originDTO().logradouro(), dto.originDTO().bairro());
		validateAddress(dto.destinationDTO().cep(), dto.destinationDTO().cidade(),
					   dto.destinationDTO().logradouro(), dto.destinationDTO().bairro());

		City cidadeOrigem = cityService.validateCity(dto.originDTO().cidade());
		City cidadeDestino = cityService.validateCity(dto.destinationDTO().cidade());

		String enderecoOrigem = String.format("%s %s", dto.originDTO().logradouro(), cidadeOrigem.getNome());
		String enderecoDestino = String.format("%s %s", dto.destinationDTO().logradouro(), cidadeDestino.getNome());

		OpenstreetmapDTO localizacaoOrigem = buscarLocalizacao(enderecoOrigem).get();
		OpenstreetmapDTO localizacaoDestino = buscarLocalizacao(enderecoDestino).get();

		Origin origem = criarOrigem(dto.originDTO(), cidadeOrigem, localizacaoOrigem);
		Destination destino = criarDestino(dto.destinationDTO(), cidadeDestino, localizacaoDestino);

		Origin origemSalva = originService.createOrigin(origem);
		Destination destinoSalvo = destinationService.createDestination(destino);

		Ride carona = new Ride();
		carona.setDriver(motorista);
		carona.setOrigin(origemSalva);
		carona.setDestination(destinoSalvo);
		carona.setDateTime(LocalDateTime.now());
		carona.setAvailableSeats(dto.vagas_disponiveis());
		carona.setStatus(rideStatusService.gellByName("ativa"));
		carona.setVehicle(veiculo);

		Ride caronaSalva = rideRepository.save(carona);
		return convertToRideDTO(caronaSalva);
	}

	public boolean validateCep(String cep, String cidade, String logradouro, String bairro) throws Exception {

			Optional<ViaCepDTO> viaCepDTO = viaCepService.buscarCep(cep);

			if(viaCepDTO.isEmpty()) {
		        throw new IllegalArgumentException("CEP destino não encontrado.");

			}
			boolean isValid =
					viaCepDTO.get().localidade().equals(cidade) &&
					viaCepDTO.get().logradouro().equals(logradouro) &&
					viaCepDTO.get().bairro().equals(bairro) ;

			if(!isValid) {
		        throw new IllegalArgumentException("Endereço não corresponde ao CEP.");

			}
			return true;
	}


	public Optional<OpenstreetmapDTO> queryOpenStreetMapByAddress(String local) {
		return openstreetmapService.buscarLocal(local);

	}


	public Ride createRide(Ride ride) {
		return rideRepository.save(ride);
	}


	public Origin convertDtoInOrigin(OriginDTO originDTO, City city, String latitude, String longitude) {
		Origin origin = new Origin();
		origin.setCity(city);
		origin.setLogradouro(originDTO.logradouro());
		origin.setNumero(originDTO.numero());
		origin.setBairro(originDTO.bairro());
		origin.setCep(originDTO.cep());
		origin.setLatitude(Double.parseDouble(latitude));
		origin.setLongitude(Double.parseDouble(longitude));

		return origin;
	}

	public Destination convertDtoInDestination(DestinationDTO destinationDTO, City city, String latitude, String longitude) {
		Destination destination = new Destination();
		destination.setCity(city);
		destination.setLogradouro(destinationDTO.logradouro());
		destination.setNumero(destinationDTO.numero());
		destination.setBairro(destinationDTO.bairro());
		destination.setCep(destinationDTO.cep());
		destination.setLatitude(Double.parseDouble(latitude));
		destination.setLongitude(Double.parseDouble(longitude));

		return destination;
	}


	public RideDTO convertToRideDTO(Ride ride) {
	    OriginDTO originDTO = new OriginDTO(
	        ride.getOrigin().getCity().getNome(),
	        ride.getOrigin().getLogradouro(),
	        ride.getOrigin().getNumero(),
	        ride.getOrigin().getBairro(),
	        ride.getOrigin().getCep()
	    );

	    DestinationDTO destinationDTO = new DestinationDTO(
	        ride.getDestination().getCity().getNome(),
	        ride.getDestination().getLogradouro(),
	        ride.getDestination().getNumero(),
	        ride.getDestination().getBairro(),
	        ride.getDestination().getCep()
	    );

	    return new RideDTO(
	        originDTO,
	        destinationDTO,
	        ride.getAvailableSeats(),
	        ride.getVehicle().getId()
	    );
	}
	@Transactional(rollbackOn = Exception.class)

	public RideDTO PostRide(Long idLong, RideDTO dto) throws Exception {
		User user = userRepository.findById(idLong).orElseThrow(() -> new RuntimeException("usuario não encontrado"));
		List<Ride>  existsDriverActives= rideRepository.findAtivasByDriverId(idLong);

		if(!existsDriverActives.isEmpty()) {
			throw new Exception("Já tem uma corrida ativa, termine a corrida atual ou cancele pra poder criar mais corridas");
		}

		vehicleService.validateUserIsVehicleOwner(idLong, dto.id_veiculo());

		Vehicle vehicle = vehicleRepository.findById(dto.id_veiculo())
			    .orElseThrow(() -> new RuntimeException("Carro não encontrado"));

		if (dto.vagas_disponiveis() <= 0 || dto.vagas_disponiveis() > vehicle.getAvailableSeats()) {
		    throw new IllegalArgumentException("A quantidade de vagas deve ser maior que 0 e menor ou igual à quantidade de assentos disponíveis no veículo escolhido" + vehicle.getAvailableSeats() );
		}

	    validateCep(dto.destinationDTO().cep(), dto.destinationDTO().cidade(), dto.destinationDTO().logradouro(), dto.destinationDTO().bairro());
	    validateCep(dto.originDTO().cep(), dto.originDTO().cidade(), dto.originDTO().logradouro(), dto.originDTO().bairro());




		City cityOrigin = cityService.validateCity(dto.originDTO().cidade());
		City cityDestination = cityService.validateCity(dto.destinationDTO().cidade());

	    String localStringOrigin =
	    	    dto.originDTO().logradouro() + " " +
	    	    cityOrigin.getNome();


	    String localEncodedOrigin = URLEncoder.encode(localStringOrigin, StandardCharsets.UTF_8);
	    System.out.println(localEncodedOrigin);


	    String localStringDestination =
	    	    dto.destinationDTO().logradouro() + " " +
	    	    cityDestination.getNome();

	    String localEncodedDestination = URLEncoder.encode(localStringDestination, StandardCharsets.UTF_8);


		Optional<OpenstreetmapDTO> resultadoOrigem = queryOpenStreetMapByAddress(localStringOrigin);
	    if (resultadoOrigem.isEmpty()) {
	        throw new RuntimeException("Erro ao buscar endereço: Endereco não encontrado");
	    }

		Optional<OpenstreetmapDTO> resultadoDestination = queryOpenStreetMapByAddress(localStringDestination);

		if (resultadoDestination.isEmpty()) {
	        throw new RuntimeException("Erro ao buscar endereço: Endereco não encontrado");
	    }


		Origin origin = convertDtoInOrigin(dto.originDTO(),cityOrigin,resultadoOrigem.get().lat(),resultadoOrigem.get().lon());

		Destination destination = convertDtoInDestination(dto.destinationDTO(),cityDestination,resultadoDestination.get().lat(),resultadoDestination.get().lon());

		Origin origemSaved =  originService.createOrigin(origin);
		Destination destinationSaved = destinationService.createDestination(destination);


		Ride ride = new Ride();
		ride.setDriver(user);
		ride.setOrigin(origin);
		ride.setDestination(destination);
		ride.setDateTime(LocalDateTime.now());
		ride.setAvailableSeats(dto.vagas_disponiveis());
		ride.setStatus(rideStatusService.gellByName("ativa"));
		ride.setVehicle(vehicle);


		Ride createdRide = createRide(ride);


		return convertToRideDTO(createdRide);
	}


	public List<RideResponseDTO> findAtivasByDriverId(Long idLong) {
		  List<Ride> ridesAtivas = rideRepository.findAtivasByDriverId(idLong);

		    List<RideResponseDTO> rideResponseList = new ArrayList<>();

		    for (Ride ride : ridesAtivas) {
		        OriginResponseDTO originDTO = new OriginResponseDTO(
		        	ride.getOrigin().getId(),
		            ride.getOrigin().getCity().getNome(),
		            ride.getOrigin().getLogradouro(),
		            ride.getOrigin().getNumero(),
		            ride.getOrigin().getBairro(),
		            ride.getOrigin().getCep()
		        );

		        DestinationResponseDTO destinationDTO = new DestinationResponseDTO(
		            ride.getDestination().getId(),
		            ride.getDestination().getCity().getNome(),
		            ride.getDestination().getLogradouro(),
		            ride.getDestination().getNumero(),
		            ride.getDestination().getBairro(),
		            ride.getDestination().getCep()
		        );

		        VehicleResponseDTO vehicleDTO = new VehicleResponseDTO(
		            ride.getVehicle().getId(),
		            ride.getVehicle().getModelo(),
		            ride.getVehicle().getMarca(),
		            ride.getVehicle().getPlaca(),
		            ride.getVehicle().getCor(),
		            ride.getVehicle().getAno(),
		            ride.getAvailableSeats()
		        );

		        RideResponseDTO rideResponse = new RideResponseDTO(
		            ride.getId(),
		            vehicleDTO,
		            originDTO,
		            destinationDTO,
		            ride.getAvailableSeats(),
		            ride.getDateTime(),
		            ride.getStatus().getNome()
		        );

		        rideResponseList.add(rideResponse);
		    }

		    return rideResponseList;
	}


	public Page<RideResponseDTO> findConcluidasyDriverId(Long idLong, int pagina, int itens) throws Exception {

		Page<Ride> findConcluidasyDriverId = rideRepository.findConcluidasyDriverId(idLong, PageRequest.of(pagina, itens));
		List<RideResponseDTO> rideResponses = new ArrayList<>();

		if(findConcluidasyDriverId.isEmpty()) {
			throw new Exception("Não há nenhuma corrida concluida");
		}

		for (Ride ride : findConcluidasyDriverId.getContent()) {
	        // Converte Origin para OriginResponseDTO
	        OriginResponseDTO originDTO = new OriginResponseDTO(
	            ride.getOrigin().getId(),
	            ride.getOrigin().getCity().getNome(),
	            ride.getOrigin().getLogradouro(),
	            ride.getOrigin().getNumero(),
	            ride.getOrigin().getBairro(),
	            ride.getOrigin().getCep()
	        );

	        // Converte Destination para DestinationResponseDTO
	        DestinationResponseDTO destinationDTO = new DestinationResponseDTO(
	            ride.getDestination().getId(),
	            ride.getDestination().getCity().getNome(),
	            ride.getDestination().getLogradouro(),
	            ride.getDestination().getNumero(),
	            ride.getDestination().getBairro(),
	            ride.getDestination().getCep()
	        );

	        // Converte Vehicle para VehicleResponseDTO
	        VehicleResponseDTO vehicleDTO = new VehicleResponseDTO(
	            ride.getVehicle().getId(),
	            ride.getVehicle().getModelo(),
	            ride.getVehicle().getMarca(),
	            ride.getVehicle().getPlaca(),
	            ride.getVehicle().getCor(),
	            ride.getVehicle().getAno(),
	            ride.getAvailableSeats()
	        );

	        // Cria RideResponse
	        RideResponseDTO rideResponse = new RideResponseDTO(
	            ride.getId(),
	            vehicleDTO,
	            originDTO,
	            destinationDTO,
	            ride.getAvailableSeats(),
	            ride.getDateTime(),
	            ride.getStatus().getNome()
	        );


	        // Adiciona à lista
	        rideResponses.add(rideResponse);
	    }


	    return new PageImpl<>(rideResponses, findConcluidasyDriverId.getPageable(), findConcluidasyDriverId.getTotalElements());
	}


	public void cancelRideByDriver(Long driverId, Long rideId) {
	    User user = userRepository.findById(driverId)
	        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

	    Ride ride = rideRepository.findById(rideId)
	        .orElseThrow(() -> new RuntimeException("Carona não encontrada"));

	    if (ride.getStatus().getNome().equalsIgnoreCase("cancelada") ||
	        ride.getStatus().getNome().equalsIgnoreCase("concluída")) {
	        throw new IllegalStateException("Caronas já concluídas ou canceladas não podem ser alteradas.");
	    }

	    if (!ride.getDriver().getId().equals(user.getId())) {
	        throw new SecurityException("Esta carona não pertence a este motorista.");
	    }

	    RideStatus rideStatus = rideStatusRepository.findByNome("cancelada");
	    ride.setStatus(rideStatus);

	    rideRepository.save(ride);
	}


	public RideDTO atualizarDriverRotas(Long driverId, RideDTO rideDTO, Long rideId) throws Exception {
	    User user = userRepository.findById(driverId)
	        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

	    Ride ride = rideRepository.findById(rideId)
	        .orElseThrow(() -> new RuntimeException("Carona não encontrada"));

	    Vehicle vehicle = vehicleRepository.findById(rideDTO.id_veiculo())
	        .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

	    if (!vehicle.getUser().getId().equals(user.getId())) {
	        throw new SecurityException("Este carro não pertence a este motorista.");
	    }

		if (rideDTO.vagas_disponiveis() <= 0 || rideDTO.vagas_disponiveis() > vehicle.getAvailableSeats()) {
		    throw new IllegalArgumentException("A quantidade de vagas deve ser maior que 0 e menor ou igual à quantidade de assentos disponíveis no veículo escolhido" + vehicle.getAvailableSeats() );
		}

	    if (ride.getStatus().getNome().equalsIgnoreCase("cancelada") ||
	        ride.getStatus().getNome().equalsIgnoreCase("concluída")) {
	        throw new IllegalStateException("Caronas já concluídas ou canceladas não podem ser alteradas.");
	    }

	    if (!ride.getDriver().getId().equals(user.getId())) {
	        throw new SecurityException("Esta carona não pertence a este motorista.");
	    }

	    validateCep(rideDTO.destinationDTO().cep(), rideDTO.destinationDTO().cidade(), rideDTO.destinationDTO().logradouro(), rideDTO.destinationDTO().bairro());
	    validateCep(rideDTO.originDTO().cep(), rideDTO.originDTO().cidade(), rideDTO.originDTO().logradouro(), rideDTO.originDTO().bairro());

	    City cityOrigin = cityService.validateCity(rideDTO.originDTO().cidade());
	    City cityDestination = cityService.validateCity(rideDTO.destinationDTO().cidade());

	    String localStringOrigin = rideDTO.originDTO().logradouro() + " " + cityOrigin.getNome();
	    String localStringDestination = rideDTO.destinationDTO().logradouro() + " " + cityDestination.getNome();

	    Optional<OpenstreetmapDTO> resultadoOrigem = queryOpenStreetMapByAddress(localStringOrigin);
	    if (resultadoOrigem.isEmpty()) {
	        throw new RuntimeException("Erro ao buscar endereço: Origem não encontrada");
	    }

	    Optional<OpenstreetmapDTO> resultadoDestination = queryOpenStreetMapByAddress(localStringDestination);
	    if (resultadoDestination.isEmpty()) {
	        throw new RuntimeException("Erro ao buscar endereço: Destino não encontrado");
	    }

	    Origin origem = originService.findById(ride.getOrigin().getId());
	    origem.setCity(cityOrigin);
	    origem.setLogradouro(rideDTO.originDTO().logradouro());
	    origem.setNumero(rideDTO.originDTO().numero());
	    origem.setBairro(rideDTO.originDTO().bairro());
	    origem.setCep(rideDTO.originDTO().cep());
	    origem.setLatitude(Double.parseDouble(resultadoOrigem.get().lat()));
	    origem.setLongitude(Double.parseDouble(resultadoOrigem.get().lon()));
	    originService.originRepository.save(origem);

	    Destination destination = destinationService.findById(ride.getDestination().getId());
	    destination.setCity(cityDestination);
	    destination.setLogradouro(rideDTO.destinationDTO().logradouro());
	    destination.setNumero(rideDTO.destinationDTO().numero());
	    destination.setBairro(rideDTO.destinationDTO().bairro());
	    destination.setCep(rideDTO.destinationDTO().cep());
	    destination.setLatitude(Double.parseDouble(resultadoDestination.get().lat()));
	    destination.setLongitude(Double.parseDouble(resultadoDestination.get().lon()));
	    destinationService.destinationRepository.save(destination);

	    ride.setAvailableSeats(rideDTO.vagas_disponiveis());


	    ride.setVehicle(vehicle); // Associar o novo veículo, se necessário
	    rideRepository.save(ride);

	    RideDTO response = new RideDTO(

	        new OriginDTO(
	            origem.getLogradouro(),
	            origem.getNumero(),
	            origem.getBairro(),
	            origem.getCep(),
	            origem.getCity().getNome()
	        ),
	        new DestinationDTO(
	            destination.getLogradouro(),
	            destination.getNumero(),
	            destination.getBairro(),
	            destination.getCep(),
	            destination.getCity().getNome()
	        ),
	        ride.getAvailableSeats(),
	        ride.getVehicle().getId()
	    );

	    return response;
	}


}
