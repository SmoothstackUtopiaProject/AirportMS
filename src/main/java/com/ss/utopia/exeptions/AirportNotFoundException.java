package com.ss.utopia.exeptions;

public class AirportNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public AirportNotFoundException() {}
	public AirportNotFoundException(String message) {
		super(message);
	}
}
