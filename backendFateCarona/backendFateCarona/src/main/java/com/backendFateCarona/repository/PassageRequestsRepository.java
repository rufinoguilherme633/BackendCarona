package com.backendFateCarona.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backendFateCarona.entity.PassageRequests;
import com.backendFateCarona.entity.User;

public interface PassageRequestsRepository extends JpaRepository<PassageRequests, Long>{

	List<PassageRequests> findAllByPassageiro(User user);

}
