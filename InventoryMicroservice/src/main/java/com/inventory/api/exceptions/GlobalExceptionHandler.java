package com.inventory.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.inventory.api.responsetemplate.GenericResponse;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> resourceNotFoundHandling(ResourceNotFoundException exception, WebRequest request) {
		
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}


	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> globalExceptionHandling(Exception exception, WebRequest request) {
		
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/*
	 * @ExceptionHandler(illegalArgument.class) public ResponseEntity<String>
	 * illegalArgumentHandling(IllegalArgumentException exception, WebRequest
	 * request) {
	 * 
	 * return new ResponseEntity<>(exception.getMessage(),
	 * HttpStatus.INTERNAL_SERVER_ERROR); }
	 */
    @ExceptionHandler(illegalArgument.class)
    @ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
    public GenericResponse<?> IllegalItemArgumentExceptionHandler(illegalArgument e, WebRequest request) {
	return new GenericResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());}
    
	@ExceptionHandler(LocationOrItemNotFoundException.class)
	public ResponseEntity<String> Lochanling(LocationOrItemNotFoundException exception, WebRequest request) {
		
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
