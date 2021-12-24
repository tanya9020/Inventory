package com.inventory.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.inventory.api.entity.Item;
@Repository
public interface ItemRepository extends MongoRepository<Item, Integer>{

	
	
	
	
	@Query("{itemName:'?0'}")
    Item findItemByName(String name);
	

}
