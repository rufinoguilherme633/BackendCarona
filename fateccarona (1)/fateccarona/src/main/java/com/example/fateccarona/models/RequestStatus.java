package com.example.fateccarona.models;

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
@Table(name = "status_solicitacao")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestStatus {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status")
    private  Integer idStatus;

  
    @Column(name = "status_nome")
    private String statusNome;
}
