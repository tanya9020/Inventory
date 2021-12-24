package com.inventory.api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Document(collection = "Item")
@RequiredArgsConstructor
public class Item {
	
	  @Id 
	  private int id;
	
	private String itemName;
	
	private String itemDesc;
	
	private String productClass;
	
	private String unitOfMeasure;
	

}
