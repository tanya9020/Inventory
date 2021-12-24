package com.inventory.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.inventory.api.entity.Location;
@Repository
public interface LocationRepository extends MongoRepository<Location, Integer>{

	
	@Query("{locationId:'?0'}")
	Location findlocByName(String s); 
	

}
