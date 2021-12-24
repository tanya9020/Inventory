package com.inventory.api.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.inventory.api.entity.Inventory;
import com.inventory.api.entity.LocationAttributes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class LocationInventoryUpdate {
	
	//private int id;
	  private Inventory inventory; private LocationAttributes location;
	
	@NotNull(message="ReasonCode cannot be null")
	private String reasonCode;
	@Min(value=0, message = "Cannot be negative")
	private double  quantityChange;
	

}
