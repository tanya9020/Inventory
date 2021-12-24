package com.inventory.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.inventory.api.entity.LocationInventory;
@Repository
public interface InventoryRepository extends MongoRepository<LocationInventory, Integer>{

	
	@Query("{'location.locationId':'?0'}")
	List<LocationInventory> findInv(String s);

	
	
	

	

}
