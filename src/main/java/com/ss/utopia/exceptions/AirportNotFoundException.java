package com.ss.utopia.exceptions;

public class AirportNotFoundException extends Exception {

	private static final long serialVersionUID = 693882186276208753L;

	public AirportNotFoundException() {}
	public AirportNotFoundException(String message) {
		super(message);
	}
}