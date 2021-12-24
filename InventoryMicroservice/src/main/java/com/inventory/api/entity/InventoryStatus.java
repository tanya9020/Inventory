package com.inventory.api.entity;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public enum InventoryStatus {
	
	@NotNull(message = "cannot be null")



	GOOD,BAD

}
