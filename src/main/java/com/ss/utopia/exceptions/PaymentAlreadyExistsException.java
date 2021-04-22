package com.ss.utopia.exceptions;

public class PaymentAlreadyExistsException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public PaymentAlreadyExistsException() {}
	public PaymentAlreadyExistsException(String message) {
		super(message);
	}
}