package com.ss.utopia.exceptions;

public class PaymentStatusNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public PaymentStatusNotFoundException() {}
	public PaymentStatusNotFoundException(String message) {
		super(message);
	}
}
