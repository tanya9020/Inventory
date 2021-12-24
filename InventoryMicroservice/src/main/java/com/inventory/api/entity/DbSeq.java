package com.inventory.api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Document(collection = "db_sequence")

@AllArgsConstructor
@NoArgsConstructor
public class DbSeq {
	
	
	@Id
	private String id;
	private int seq;
	


	

}
