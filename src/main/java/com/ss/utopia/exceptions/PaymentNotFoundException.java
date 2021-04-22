package com.ss.utopia.exceptions;

public class PaymentNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public PaymentNotFoundException() {}
	public PaymentNotFoundException(String message) {
		super(message);
	}
}
