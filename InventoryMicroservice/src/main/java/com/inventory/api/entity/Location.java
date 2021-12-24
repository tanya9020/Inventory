package com.inventory.api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
@Data
@AllArgsConstructor
@Document(collection = "Location")
@RequiredArgsConstructor
public class Location {

	@Id
	private int id;
	
	private String locationId;
	private String enterpriseCode;
	private String node;
	private String zone;
	
}
