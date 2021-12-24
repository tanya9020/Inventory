package com.inventory.api.dto;

import com.inventory.api.entity.Inventory;
import com.inventory.api.entity.LocationAttributes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class LocationInventoryCreate {

	
	private Inventory inventory;
	private LocationAttributes location;
	private String reasonCode;


}
