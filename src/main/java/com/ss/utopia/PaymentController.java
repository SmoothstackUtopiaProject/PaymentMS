package com.ss.utopia;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ss.utopia.exceptions.PaymentAlreadyExistsException;
import com.ss.utopia.exceptions.PaymentNotFoundException;
import com.ss.utopia.exceptions.PaymentStatusNotFoundException;
import com.ss.utopia.models.Payment;
import com.ss.utopia.models.ErrorMessage;
import com.ss.utopia.services.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;
	
	@GetMapping("/health")
	public ResponseEntity<Object> health() {
		return new ResponseEntity<>("\"status\": \"up\"", HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Object> findAll() {
		List<Payment> payments = paymentService.findAll();
		return !payments.isEmpty()
			? new ResponseEntity<>(payments, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("{paymentId}")
	public ResponseEntity<Object> findById(@PathVariable String paymentId) 
	throws PaymentNotFoundException {
		Integer formattedId = Integer.parseInt(paymentId);
		Payment payment = paymentService.findById(formattedId);
		return new ResponseEntity<>(payment, HttpStatus.OK);
	}

	@GetMapping("/booking/{bookingId}")
	public ResponseEntity<Object> findByBookingUuid(@PathVariable String bookingId) 
	throws PaymentNotFoundException {
		Payment payment = paymentService.findByBookingUuid(bookingId);
		return new ResponseEntity<>(payment, HttpStatus.OK);
	}

	@GetMapping("/stripe/{stripeId}")
	public ResponseEntity<Object> findByStripeUuid(@PathVariable String stripeId)
	throws PaymentNotFoundException  {
		Payment payment = paymentService.findByStripeUuid(stripeId);
		return new ResponseEntity<>(payment, HttpStatus.OK);
	}

	@PostMapping("/search")
	public ResponseEntity<Object> findBySearchAndFilter(@RequestBody Map<String, String> filterMap) {
		List<Payment> payments = paymentService.findBySearchAndFilter(filterMap);
		return !payments.isEmpty()
			? new ResponseEntity<>(payments, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping
	public ResponseEntity<Object> insert(@RequestBody Map<String, String> paymentMap) 
	throws PaymentAlreadyExistsException, PaymentStatusNotFoundException {
		String paymentBookingUuid = paymentMap.get("paymentBookingUuid");
		String paymentStripeUuid = paymentMap.get("paymentStripeUuid");
		String paymentStatus = paymentMap.get("paymentStatus");

		Payment newPayment = paymentService.insert(paymentBookingUuid, paymentStripeUuid, paymentStatus);
		return new ResponseEntity<>(newPayment, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<Object> update(@RequestBody Map<String, String> paymentMap) 
	throws PaymentAlreadyExistsException, PaymentNotFoundException, PaymentStatusNotFoundException {
		Integer paymentId = Integer.parseInt(paymentMap.get("paymentId"));
		String paymentBookingUuid = paymentMap.get("paymentBookingUuid");
		String paymentStripeUuid = paymentMap.get("paymentStripeUuid");
		String paymentStatus = paymentMap.get("paymentStatus");

		Payment newPayment = paymentService.update(paymentId, paymentBookingUuid, paymentStripeUuid, paymentStatus);
		return new ResponseEntity<>(newPayment, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("{paymentId}")
	public ResponseEntity<Object> delete(@PathVariable String paymentId) 
	throws IllegalArgumentException, PaymentNotFoundException {
		Integer formattedId = Integer.parseInt(paymentId);
		String deleteInformation = paymentService.delete(formattedId);
		return new ResponseEntity<>(deleteInformation, HttpStatus.ACCEPTED);
	}

	// Exception Handling
	// ========================================================================
	@ExceptionHandler(PaymentAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<Object> paymentAlreadyExistsException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.CONFLICT
		);
	}

	@ExceptionHandler(PaymentNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> paymentNotFoundException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.NOT_FOUND
		);
	}

	@ExceptionHandler(PaymentStatusNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> paymentStatusNotFoundException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.BAD_REQUEST
		);
	}

	@ExceptionHandler(ConnectException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ResponseEntity<Object> invalidConnection() {
		return new ResponseEntity<>(
			new ErrorMessage("Service temporarily unavailabe."), 
			HttpStatus.SERVICE_UNAVAILABLE
		);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> invalidMessage() {
		return new ResponseEntity<>(
			new ErrorMessage("Invalid HTTP message content."), 
			HttpStatus.BAD_REQUEST
		);
	}

	@ExceptionHandler(NumberFormatException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> invalidParameters(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.BAD_REQUEST
		);
	}

	@ExceptionHandler(SQLException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ResponseEntity<Object> invalidSQL() {
		return new ResponseEntity<>(
			new ErrorMessage("Service temporarily unavailabe."), 
			HttpStatus.SERVICE_UNAVAILABLE
		);
	}
}
