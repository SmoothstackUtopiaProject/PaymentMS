package com.ss.utopia.exceptions;

public class PassengerAlreadyExistsException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public PassengerAlreadyExistsException() {}
	public PassengerAlreadyExistsException(String message) {
		super(message);
	}
}