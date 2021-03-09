package com.ss.utopia.exceptions;

public class AirportNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public AirportNotFoundException() {}
	public AirportNotFoundException(String message) {
		super(message);
	}
}