package com.inventory.api.entity;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Items {
	@NotNull(message="Item name cannot be null")
	private String itemName;
	private String itemDesc;
	
	  @NotNull(message="Product Class cannot be null") private String productClass;
	 
	@NotNull(message="UOM cannot be null")
	private String unitOfMeasure;
}
