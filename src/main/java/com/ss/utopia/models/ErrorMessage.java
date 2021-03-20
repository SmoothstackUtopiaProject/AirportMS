package com.ss.utopia.models;

public class ErrorMessage {

	private String error;

	public ErrorMessage() {}
	public ErrorMessage(String error) {
		this.error = error;
	}

	public String getError() {
		return this.error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
