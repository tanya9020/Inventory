package com.inventory.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.inventory.api.entity.Location;
import com.inventory.api.entity.ReasonCode;

public interface ReasonCodeRepository extends MongoRepository<ReasonCode, Integer> {
	
	@Query("{reasonCode:'?0'}")
	ReasonCode findReasonCode(String s); 

}
