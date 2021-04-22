package com.ss.utopia.services;

import java.util.Map;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exceptions.PaymentAlreadyExistsException;
import com.ss.utopia.exceptions.PaymentNotFoundException;
import com.ss.utopia.exceptions.PaymentStatusNotFoundException;
import com.ss.utopia.filters.PaymentFilters;
import com.ss.utopia.models.Payment;
import com.ss.utopia.repositories.PaymentRepository;

@Service
public class PaymentService {

	private final String[] validPaymentStatuses = { "PENDING", "CONFIRMED", "APPROVED", "REJECTED" };

	@Autowired
	private PaymentRepository paymentRepository;


	public List<Payment> findAll() {
		return paymentRepository.findAll();
	}

	public Payment findById(Integer id) throws PaymentNotFoundException {
		Optional<Payment> optionalPayment = paymentRepository.findById(id);
		if(!optionalPayment.isPresent()) {
			throw new PaymentNotFoundException("No Payment with ID: " + id + " exist!");
		}
		return optionalPayment.get();
	}

	public Payment findByBookingUuid(String paymentBookingUuid) throws PaymentNotFoundException {
		Optional<Payment> optionalPayment = paymentRepository.findByBookingUuid(paymentBookingUuid);
		if(!optionalPayment.isPresent()) {
			throw new PaymentNotFoundException("No Payment with Booking ID: " + paymentBookingUuid + " exist!");
		}
		return optionalPayment.get();
	}

	public Payment findByStripeUuid(String paymentStripeUuid) throws PaymentNotFoundException {
		Optional<Payment> optionalPayment = paymentRepository.findByStripeUuid(paymentStripeUuid);
		if(!optionalPayment.isPresent()) {
			throw new PaymentNotFoundException("No Payment with Passport ID: " + paymentStripeUuid + " exist!");
		}
		return optionalPayment.get();
	}

	public List<Payment> findBySearchAndFilter(Map<String, String> filterMap) {
		List<Payment> payments = findAll();
		if(!filterMap.keySet().isEmpty()) {
			payments = PaymentFilters.apply(payments, filterMap);
		}
		return payments;
	}

	public Payment insert(String paymentBookingUuid, String paymentStripeUuid, String paymentStatus)
		throws PaymentAlreadyExistsException, PaymentStatusNotFoundException {

		// Validate Payment Status
		if(Boolean.FALSE.equals(validatePaymentStatus(paymentStatus))) {
			throw new PaymentStatusNotFoundException(
				"\"" + paymentStatus + "\" is not a valid Payment Status. Acceptable Payment Statuses are: \"" +
				Arrays.toString(validPaymentStatuses) + "\""
			);
		}

		// Verify a Payment with this Stripe Uuid does not already exist
		try {
			findByStripeUuid(paymentStripeUuid);
			throw new PaymentAlreadyExistsException(
				"A Payment with the Stripe UUID: " + 
				paymentStripeUuid + " already exists."
			);
		} 
		catch(PaymentNotFoundException err) {
			return paymentRepository.save(
				new Payment(paymentBookingUuid, paymentStripeUuid, paymentStatus)
			);
		}
	}

	public Payment update(Integer paymentId, String paymentBookingUuid, String paymentStripeUuid, String paymentStatus)
		throws PaymentAlreadyExistsException, PaymentNotFoundException, PaymentStatusNotFoundException {	

		// Validate Payment Status
		if(Boolean.FALSE.equals(validatePaymentStatus(paymentStatus))) {
			throw new PaymentStatusNotFoundException(
				"\"" + paymentStatus + "\" is not a valid Payment Status. Acceptable Payment Statuses are: \"" +
				Arrays.toString(validPaymentStatuses) + "\""
			);
		}

		// Verify the Payment exists
		Payment currentPayment = findById(paymentId);

		// If changing the Stripe UUID, verify a Payment with this Stripe UUID does not already exist
		if(!currentPayment.getPaymentStripeUuid().equals(paymentStripeUuid)) {
			try {
				findByStripeUuid(paymentStripeUuid);
				throw new PaymentAlreadyExistsException(
					"A Payment with the Stripe UUID: " + 
					paymentStripeUuid + " already exists."
				);
			} 
			catch(PaymentNotFoundException err) {/* Do nothing - payment not found means we're good to go */}
		}
		return paymentRepository.save(new Payment(paymentId, paymentBookingUuid, paymentStripeUuid, paymentStatus));
	}

	public String delete(Integer id) throws IllegalArgumentException, PaymentNotFoundException {
		findById(id);
		paymentRepository.deleteById(id);
		return "Payment with ID: " + id + " was deleted.";
	}

	private Boolean validatePaymentStatus(String paymentStatus) {
		for(String validStatus : validPaymentStatuses) {
			if(validStatus.equals(paymentStatus)) {
				return true;
			}
		}
		return false;
	}
}
