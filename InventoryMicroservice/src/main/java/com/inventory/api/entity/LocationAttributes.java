package com.inventory.api.entity;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Document
@AllArgsConstructor
@RequiredArgsConstructor
public class LocationAttributes {
	
	@NotNull(message="LocationId cannot be null")
	private String locationId;
	@NotNull(message="Zone cannot be null")
	private String zone;
	@NotNull(message="Enterprise Code cannot be null")
	private String enterpriseCode;
	@NotNull(message="Node cannot be null")
	private String node;
	
}
