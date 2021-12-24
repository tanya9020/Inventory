package com.inventory.api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Document(collection = "LocationInventory")
@AllArgsConstructor
@RequiredArgsConstructor
public class LocationInventory extends AuditMetadata {
	
	@Transient
	public static final String SEQUENCE_NAME = "user_sequence";
	
	@Id
	private int id;
	
	



private Inventory inventory;
private LocationAttributes location;

	 @Version long version; 

}
