package com.ss.utopia.models;

public class HttpError {

	private String error;
	private Integer status;

	public HttpError(String error, Integer status) {
		this.error = error;
		this.status = status;
	}

	public String getError() {
		return this.error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
