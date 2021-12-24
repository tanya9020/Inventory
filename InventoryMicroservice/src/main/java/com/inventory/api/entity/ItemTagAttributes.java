package com.inventory.api.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemTagAttributes {
	private String batchNo;
	
	private List<String> serialList;

}
