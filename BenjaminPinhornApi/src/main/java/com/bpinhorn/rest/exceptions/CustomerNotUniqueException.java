package com.bpinhorn.rest.exceptions;

public class CustomerNotUniqueException extends RuntimeException {

	public CustomerNotUniqueException(String message) {
		super(message);
	}
	
	public CustomerNotUniqueException(String message, Throwable cause) {
		super(message, cause);
	}

}
