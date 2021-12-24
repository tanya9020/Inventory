package com.inventory.api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Document(collection = "ReasonCode")
@RequiredArgsConstructor
public class ReasonCode {
	
	@Id
	private String id;
	private String reasonCode;

}
