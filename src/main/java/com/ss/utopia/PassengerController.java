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

import com.ss.utopia.exceptions.PassengerAlreadyExistsException;
import com.ss.utopia.exceptions.PassengerNotFoundException;
import com.ss.utopia.models.Passenger;
import com.ss.utopia.models.ErrorMessage;
import com.ss.utopia.services.PassengerService;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

	@Autowired
	private PassengerService passengerService;
	
	@GetMapping("/health")
	public ResponseEntity<Object> health() {
		return new ResponseEntity<>("\"status\": \"up\"", HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Object> findAll() {
		List<Passenger> passengers = passengerService.findAll();
		return !passengers.isEmpty()
			? new ResponseEntity<>(passengers, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("{passengerId}")
	public ResponseEntity<Object> findById(@PathVariable String passengerId) 
	throws PassengerNotFoundException {
		Integer formattedId = Integer.parseInt(passengerId);
		Passenger passenger = passengerService.findById(formattedId);
		return new ResponseEntity<>(passenger, HttpStatus.OK);
	}

	@GetMapping("/booking/{bookingId}")
	public ResponseEntity<Object> findByBookingId(@PathVariable String bookingId) 
	throws PassengerNotFoundException {
		Integer formattedBookingId = Integer.parseInt(bookingId);
		Passenger passenger = passengerService.findByBookingId(formattedBookingId);
		return new ResponseEntity<>(passenger, HttpStatus.OK);
	}

	@GetMapping("/passport/{passportId}")
	public ResponseEntity<Object> findByPassportId(@PathVariable String passportId)
	throws PassengerNotFoundException  {
		Passenger passenger = passengerService.findByPassportId(passportId);
		return new ResponseEntity<>(passenger, HttpStatus.OK);
	}

	@PostMapping("/search")
	public ResponseEntity<Object> findBySearchAndFilter(@RequestBody Map<String, String> filterMap) {
		List<Passenger> passengers = passengerService.findBySearchAndFilter(filterMap);
		return !passengers.isEmpty()
			? new ResponseEntity<>(passengers, HttpStatus.OK)
			: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping
	public ResponseEntity<Object> insert(@RequestBody Map<String, String> passengerMap) 
	throws PassengerAlreadyExistsException {
		Integer passengerBookingId = Integer.parseInt(passengerMap.get("passengerBookingId"));
		String passengerPassportId = passengerMap.get("passengerPassportId");
		String passengerFirstName = passengerMap.get("passengerFirstName");
		String passengerLastName = passengerMap.get("passengerLastName");
		String passengerDateOfBirth = passengerMap.get("passengerDateOfBirth");
		String passengerSex = passengerMap.get("passengerSex");
		String passengerAddress = passengerMap.get("passengerAddress");
		Boolean passengerIsVeteran = Boolean.parseBoolean(passengerMap.get("passengerIsVeteran"));

		Passenger newPassenger = passengerService.insert(
			passengerBookingId, passengerPassportId, passengerFirstName, 
			passengerLastName, passengerDateOfBirth, passengerSex, passengerAddress, 
			passengerIsVeteran
		);
		return new ResponseEntity<>(newPassenger, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<Object> update(@RequestBody Map<String, String> passengerMap) 
	throws PassengerNotFoundException {
		Integer passengerId = Integer.parseInt(passengerMap.get("passengerId"));
		Integer passengerBookingId = Integer.parseInt(passengerMap.get("passengerBookingId"));
		String passengerPassportId = passengerMap.get("passengerPassportId");
		String passengerFirstName = passengerMap.get("passengerFirstName");
		String passengerLastName = passengerMap.get("passengerLastName");
		String passengerDateOfBirth = passengerMap.get("passengerDateOfBirth");
		String passengerSex = passengerMap.get("passengerSex");
		String passengerAddress = passengerMap.get("passengerAddress");
		Boolean passengerIsVeteran = Boolean.parseBoolean(passengerMap.get("passengerIsVeteran"));

		Passenger newPassenger = passengerService.update(
			passengerId, passengerBookingId, passengerPassportId, 
			passengerFirstName, passengerLastName, passengerDateOfBirth, 
			passengerSex, passengerAddress, passengerIsVeteran
		);
		return new ResponseEntity<>(newPassenger, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("{passengerId}")
	public ResponseEntity<Object> delete(@PathVariable String passengerId) 
	throws IllegalArgumentException, PassengerNotFoundException {
		Integer formattedId = Integer.parseInt(passengerId);
		String deleteInformation = passengerService.delete(formattedId);
		return new ResponseEntity<>(deleteInformation, HttpStatus.ACCEPTED);
	}

	// Exception Handling
	// ========================================================================
	@ExceptionHandler(PassengerAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<Object> passengerAlreadyExistsException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.CONFLICT
		);
	}

	@ExceptionHandler(PassengerNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> passengerNotFoundException(Throwable err) {
		return new ResponseEntity<>(
			new ErrorMessage(err.getMessage()), 
			HttpStatus.NOT_FOUND
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
