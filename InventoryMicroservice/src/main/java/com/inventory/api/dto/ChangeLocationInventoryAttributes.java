package com.inventory.api.dto;

import com.inventory.api.entity.Inventory;
import com.inventory.api.entity.LocationAttributes;
import com.inventory.api.entity.ToInventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ChangeLocationInventoryAttributes {
	

	//private Inventory inventory;
	//private int id;
	private  Inventory fromInventory;
	private  ToInventory toInventory;
	private LocationAttributes location;
	private String reasonCode;
	

}
