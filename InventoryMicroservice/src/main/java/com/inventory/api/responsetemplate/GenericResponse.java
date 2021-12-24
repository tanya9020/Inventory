package com.inventory.api.responsetemplate;




import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.json.JsonMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
//import com.fasterxml.jackson.datatype:jackson-datatype-jsr310;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class GenericResponse<T> {
    private HttpStatus status;
    private LocalDateTime timestamp;
    
    private T response;
    

    public GenericResponse(HttpStatus status, T response) {

	this.status = status;
	this.response = response;	

	//this.timestamp = Instant.now();
	
	this.timestamp = LocalDateTime.now();
		
		/*
		 * ObjectMapper mapper = JsonMapper.builder() .addModule(new JavaTimeModule())
		 * .build();
		 */
	
		 

    }
    
}
