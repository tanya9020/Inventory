package com.inventory.api.exceptions;

public class illegalArgument extends Exception {
private static final String ErrorMessage = "Invalid arguments";
	private static final long serialVersionUID = 1L;

	
    public illegalArgument() {
    	
    	
	//super(ErrorMessage);// TODO Auto-generated constructor stub
    }
    public illegalArgument(String m) {
    	
    	
    	super(m);// TODO Auto-generated constructor stub
        }

}
