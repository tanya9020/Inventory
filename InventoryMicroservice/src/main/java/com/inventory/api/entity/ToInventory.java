package com.inventory.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Data
@RequiredArgsConstructor
public class ToInventory {
	private String inventoryStatus;
	private String productClass;
	private String shipByDate;
	private String batchNo;
	
	
	

}
