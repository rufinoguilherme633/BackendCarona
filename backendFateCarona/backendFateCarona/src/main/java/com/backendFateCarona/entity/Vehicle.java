package com.backendFateCarona.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="veiculos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id_veiculo")
	    private Long idVeiculo;
	    @OneToOne
	    @JoinColumn(name = "id_usuario", nullable = false)  // Esta é a chave estrangeira
	    @JsonBackReference
	    private User usuario; 
	    private String modelo;
	    private String marca;
	    @Column(unique = true)
	    private String placa;
	    private String cor;
	    private Integer ano;
}
