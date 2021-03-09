package com.ss.utopia.exceptions;

public class AirportAlreadyExistsException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public AirportAlreadyExistsException() {}
	public AirportAlreadyExistsException(String message) {
		super(message);
	}
}