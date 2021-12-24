package com.inventory.api.exceptions;

public class LocationOrItemNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public LocationOrItemNotFoundException(String message) {
		super(message);
	}

}
