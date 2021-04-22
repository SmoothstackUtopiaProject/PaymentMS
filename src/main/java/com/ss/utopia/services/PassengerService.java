package com.ss.utopia.services;

import java.util.Map;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exceptions.PassengerAlreadyExistsException;
import com.ss.utopia.exceptions.PassengerNotFoundException;
import com.ss.utopia.filters.PassengerFilters;
import com.ss.utopia.models.Passenger;
import com.ss.utopia.repositories.PassengerRepository;

@Service
public class PassengerService {

	@Autowired
	private PassengerRepository passengerRepository;

	public List<Passenger> findAll() {
		return passengerRepository.findAll();
	}

	public Passenger findById(Integer id) throws PassengerNotFoundException {
		Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
		if(!optionalPassenger.isPresent()) {
			throw new PassengerNotFoundException("No Passenger with ID: " + id + " exist!");
		}
		return optionalPassenger.get();
	}

	public Passenger findByBookingId(Integer passengerBookingId) throws PassengerNotFoundException {
		Optional<Passenger> optionalPassenger = passengerRepository.findByBookingId(passengerBookingId);
		if(!optionalPassenger.isPresent()) {
			throw new PassengerNotFoundException("No Passenger with Booking ID: " + passengerBookingId + " exist!");
		}
		return optionalPassenger.get();
	}

	public Passenger findByPassportId(String passengerPassportId) throws PassengerNotFoundException {
		Optional<Passenger> optionalPassenger = passengerRepository.findByPassportId(passengerPassportId);
		if(!optionalPassenger.isPresent()) {
			throw new PassengerNotFoundException("No Passenger with Passport ID: " + passengerPassportId + " exist!");
		}
		return optionalPassenger.get();
	}

	public List<Passenger> findBySearchAndFilter(Map<String, String> filterMap) {
		List<Passenger> passengers = findAll();
		if(!filterMap.keySet().isEmpty()) {
			passengers = PassengerFilters.apply(passengers, filterMap);
		}
		return passengers;
	}

	public Passenger insert(Integer passengerBookingId, String passengerPassportId, String passengerFirstName, 
		String passengerLastName, String passengerDateOfBirth, String passengerSex, String passengerAddress,
		Boolean passengerIsVeteran) throws PassengerAlreadyExistsException {

		// Verify a Passenger with this Passport ID does not already exist
		try {
			findByPassportId(passengerPassportId);
			throw new PassengerAlreadyExistsException(
				"A Passenger with the Passport ID: " + 
				passengerPassportId + " already exists."
			);
		} 
		catch(PassengerNotFoundException err) {
			return passengerRepository.save(new Passenger(
				passengerBookingId, passengerPassportId, passengerFirstName, passengerLastName, 
				passengerDateOfBirth, passengerSex, passengerAddress, passengerIsVeteran
			));
		}
	}

	public Passenger update(Integer passengerId, Integer passengerBookingId, String passengerPassportId, 
	String passengerFirstName, String passengerLastName, String passengerDateOfBirth, String passengerSex, 
	String passengerAddress, Boolean passengerIsVeteran) throws PassengerNotFoundException {	

		findById(passengerId);
		return passengerRepository.save(new Passenger(
			passengerId, passengerBookingId, passengerPassportId, passengerFirstName, passengerLastName,
			passengerDateOfBirth, passengerSex, passengerAddress, passengerIsVeteran
		));
	}

	public String delete(Integer id) throws IllegalArgumentException, PassengerNotFoundException {
		findById(id);
		passengerRepository.deleteById(id);
		return "Passenger with ID: " + id + " was deleted.";
	}
}