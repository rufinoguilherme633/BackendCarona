package com.example.fatecCarCarona.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="status_carona")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RideStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_status_carona")
	Long id;
	@Column(name ="status_nome")
	String nome;
	
}
