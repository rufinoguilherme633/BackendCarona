package com.backendFateCarona.entity;

import java.time.LocalDateTime;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "solicitacoes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PassageRequests {
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	@Column(name = "id_solicitacao")
	Long  idSolicitacao;
	
	@ManyToOne	
	@JoinColumn(name = "id_carona")
	RideManagement carona;
	
	@ManyToOne	
	@JoinColumn(name = "id_passageiro ")
	User passageiro ;
	
	@Column(name="data_hora")
	LocalDateTime dataHora;
	
	String origem;

	@Column(name = "longitude_origem")
	Double longitudeOrigem;
	
	
	@Column(name = "latitude_origem")
	Double latitudeOrigem;
	
	
	String destino;
	
	@Column(name = "longitude_destino")
	Double longitudeDestino;
	
	
	@Column(name = "latitude_destino")
	Double latitudeDestino;
	
	@Enumerated(EnumType.STRING)
	@JoinColumn(name="status")
	RequestStatus status;
}
