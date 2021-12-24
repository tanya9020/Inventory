package com.inventory.api.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;



@AllArgsConstructor
@Data
@RequiredArgsConstructor

public class Inventory {
	
	@NotNull(message="inventoryStatus cannot be null")
	private String inventoryStatus;
	
	@Min(value=0, message = "Quantity Cannot be negative")
	private double quantity;
	@NotNull(message="Ship By Date cannot be null")
	private String shipByDate;
	
	private ItemAttributes itemAttributes;
	

}
