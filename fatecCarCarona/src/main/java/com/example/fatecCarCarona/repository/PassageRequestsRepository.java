package com.example.fatecCarCarona.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.fatecCarCarona.entity.PassageRequests;

public interface PassageRequestsRepository extends JpaRepository<PassageRequests, Long> {
	@Query("SELECT p FROM PassageRequests p WHERE p.passageiro.id = :userId AND p.status.nome = 'concluída'")
	Page<PassageRequests> findPassagerConcluidas(Long userId, PageRequest of);
	
	@Query("SELECT p FROM PassageRequests p WHERE p.passageiro.id = :userId AND p.status.nome IN ('aceita', 'recusada', 'cancelada', 'concluida')")
	Page<PassageRequests> findPassagerFinalizadas(Long userId, PageRequest pageRequest);
	
	@Query("SELECT p FROM PassageRequests p WHERE p.passageiro.id = :userId AND p.status.nome = 'pendente'")
	PassageRequests findByPassagePending(Long userId);


	@Query("""
		    SELECT p FROM PassageRequests p 
		    JOIN p.carona c 
		    WHERE p.status.id = 1
		      AND c.status.id = 1
		      AND c.driver.id = :driverId
		""")
		List<PassageRequests> requestsForMyRide(Long driverId);


}
