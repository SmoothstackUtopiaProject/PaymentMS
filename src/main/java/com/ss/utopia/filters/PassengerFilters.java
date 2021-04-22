package com.ss.utopia.filters;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.ss.utopia.models.Passenger;

public final class PassengerFilters {

  private static final String PASSENGER_ID = "passengerId";
  private static final String PASSENGER_ID_BOOKING = "passengerBookingId";
  private static final String PASSENGER_ID_PASSPORT = "passengerPassportId";
  private static final String PASSENGER_NAME_FIRST = "passengerFirstName";
  private static final String PASSENGER_NAME_LAST = "passengerLastName";
  private static final String PASSENGER_DATE_OF_BIRTH = "passengerDateOfBirth";
  private static final String PASSENGER_SEX = "passengerSex";
  private static final String PASSENGER_ADDRESS = "passengerAddress";
  private static final String PASSENGER_ISVETERAN = "passengerIsVeteran";
  private static final String PASSENGER_AGE = "passengerAge";
  private static final String PASSENGER_AGE_GREATER_THAN = "passengerAgeGreaterThan";
  private static final String PASSENGER_AGE_LESS_THAN = "passengerAgeLessThan";
  private static final String SEARCH_TERMS = "searchTerms";

  private PassengerFilters() {
    throw new IllegalStateException("Utilility class 'PassengerFilters' is static and should not be instantiated.");
  }

  public static List<Passenger> apply(List<Passenger> passengers, Map<String, String> filterMap) {
    
    // ID
		if(filterMap.keySet().contains(PASSENGER_ID)) {
			Integer parsedPassengerId = Integer.parseInt(filterMap.get(PASSENGER_ID));
			passengers = PassengerFilters.filterByPassengerId(passengers, parsedPassengerId);
		}

		// Booking ID
		if(filterMap.keySet().contains(PASSENGER_ID_BOOKING)) {
			Integer parsedPassengerBookingId = Integer.parseInt(filterMap.get(PASSENGER_ID_BOOKING));
			passengers = PassengerFilters.filterByPassengerBooking(passengers, parsedPassengerBookingId);
		}

		// Passport ID
		if(filterMap.keySet().contains(PASSENGER_ID_PASSPORT)) {
			passengers = PassengerFilters.filterByPassengerPassport(passengers, filterMap.get(PASSENGER_ID_PASSPORT));
		}

		// First Name
		if(filterMap.keySet().contains(PASSENGER_NAME_FIRST)) {
			passengers = PassengerFilters.filterByPassengerFirstName(passengers, filterMap.get(PASSENGER_NAME_FIRST));
		}

		// Last Name
		if(filterMap.keySet().contains(PASSENGER_NAME_LAST)) {
			passengers = PassengerFilters.filterByPassengerLastName(passengers, filterMap.get(PASSENGER_NAME_LAST));
		}

		// DateOfBirth
		if(filterMap.keySet().contains(PASSENGER_DATE_OF_BIRTH)) {
			passengers = PassengerFilters.filterByPassengerDateOfBirth(passengers, filterMap.get(PASSENGER_DATE_OF_BIRTH));
		}

		// Sex
		if(filterMap.keySet().contains(PASSENGER_SEX)) {
			passengers = PassengerFilters.filterByPassengerSex(passengers, filterMap.get(PASSENGER_SEX));
		}

		// Address
		if(filterMap.keySet().contains(PASSENGER_ADDRESS)) {
			passengers = PassengerFilters.filterByPassengerAddress(passengers, filterMap.get(PASSENGER_ADDRESS));
		}

		// Veteran
		if(filterMap.keySet().contains(PASSENGER_ISVETERAN)) {
      Boolean parsedPassengerIsVet = Boolean.valueOf(filterMap.get(PASSENGER_ISVETERAN));
      passengers = PassengerFilters.filterByPassengerIsVeteran(passengers, parsedPassengerIsVet);
		}

		// Age - Exact Match
		if(filterMap.keySet().contains(PASSENGER_AGE)) {
      Integer parsedPassengerAge = Integer.parseInt(filterMap.get(PASSENGER_AGE));
      passengers = PassengerFilters.filterByPassengerAge(passengers, parsedPassengerAge);
		}	

		// Age - Greater Than
		if(filterMap.keySet().contains(PASSENGER_AGE_GREATER_THAN)) {
      Integer parsedPassengerAge = Integer.parseInt(filterMap.get(PASSENGER_AGE_GREATER_THAN));
      passengers = PassengerFilters.filterByPassengerAgeGreaterThan(passengers, parsedPassengerAge);
		}	

		// Age - Less Than
		if(filterMap.keySet().contains(PASSENGER_AGE_LESS_THAN)) {
      Integer parsedPassengerAge = Integer.parseInt(filterMap.get(PASSENGER_AGE_LESS_THAN));
      passengers = PassengerFilters.filterByPassengerAgeLessThan(passengers, parsedPassengerAge);
		}

    // SearchTerms
    if(filterMap.keySet().contains(SEARCH_TERMS)) {
      passengers = PassengerFilters.filterBySearchTerms(passengers, filterMap.get(SEARCH_TERMS));
    }

		return passengers;
  }

  public static List<Passenger> filterByPassengerAddress 
  (Collection<Passenger> passengers, String passengerAddress) {
    return passengers.parallelStream()
      .filter(i -> i.getPassengerAddress().equals(passengerAddress))
      .collect(Collectors.toList());
  }

  public static List<Passenger> filterByPassengerAge 
  (Collection<Passenger> passengers, Integer passengerAge) {
    return passengers.parallelStream()
      .filter(i -> Period.between(
        LocalDate.parse(i.getPassengerDateOfBirth()), 
        LocalDate.now()
      )
      .getYears() == (passengerAge))
      .collect(Collectors.toList());
  }

  public static List<Passenger> filterByPassengerAgeGreaterThan
  (Collection<Passenger> passengers, Integer passengerAge) {
    return passengers.parallelStream()
      .filter(i -> Period.between(
         LocalDate.parse(i.getPassengerDateOfBirth()), 
        LocalDate.now()
      )
      .getYears() > (passengerAge))
      .collect(Collectors.toList());
  }

  public static List<Passenger> filterByPassengerAgeLessThan 
  (Collection<Passenger> passengers, Integer passengerAge) {
    return passengers.parallelStream()
      .filter(i -> Period.between(
         LocalDate.parse(i.getPassengerDateOfBirth()), 
        LocalDate.now()
      )
      .getYears() < (passengerAge))
      .collect(Collectors.toList());
  }

  public static List<Passenger> filterByPassengerBooking 
  (Collection<Passenger> passengers, Integer passengerBookingId) {
    return passengers.parallelStream()
      .filter(i -> i.getPassengerBookingId().equals(passengerBookingId))
      .collect(Collectors.toList());
  }

  public static List<Passenger> filterByPassengerDateOfBirth 
  (Collection<Passenger> passengers, String passengerDateOfBirth) {
    return passengers.parallelStream()
      .filter(i -> i.getPassengerDateOfBirth().equals(passengerDateOfBirth))
      .collect(Collectors.toList());
  }

  public static List<Passenger> filterByPassengerFirstName 
  (Collection<Passenger> passengers, String passengerFirstName) {
    return passengers.parallelStream()
      .filter(i -> i.getPassengerFirstName().equals(passengerFirstName))
      .collect(Collectors.toList());
  }

  public static List<Passenger> filterByPassengerId 
  (Collection<Passenger> passengers, Integer passengerId) {
    return passengers.parallelStream()
      .filter(i -> i.getPassengerId().equals(passengerId))
      .collect(Collectors.toList());
  }

  public static List<Passenger> filterByPassengerIsVeteran 
  (Collection<Passenger> passengers, Boolean passengerIsVeteran) {
    return passengers.parallelStream()
      .filter(i -> i.getPassengerIsVeteran().equals(passengerIsVeteran))
      .collect(Collectors.toList());
  }

  public static List<Passenger> filterByPassengerLastName 
  (Collection<Passenger> passengers, String passengerLastName) {
    return passengers.parallelStream()
      .filter(i -> i.getPassengerLastName().equals(passengerLastName))
      .collect(Collectors.toList());
  }

  public static List<Passenger> filterByPassengerPassport 
  (Collection<Passenger> passengers, String passengerPassportId) {
    return passengers.parallelStream()
      .filter(i -> i.getPassengerPassportId().equals(passengerPassportId))
      .collect(Collectors.toList());
  }

  public static List<Passenger> filterByPassengerSex 
  (Collection<Passenger> passengers, String passengerSex) {
    return passengers.parallelStream()
      .filter(i -> i.getPassengerSex().equals(passengerSex))
      .collect(Collectors.toList());
  }

  public static List<Passenger> filterBySearchTerms(List<Passenger> passengers, String searchTerms) {
    String formattedSearch = searchTerms.toLowerCase(Locale.getDefault()).replace(", ", ",");
    String[] splitTerms = formattedSearch.split(",");

    for(String searchTerm : splitTerms) {
      passengers = passengers.parallelStream()
      .filter((Passenger i) ->
          i.getPassengerId().toString().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
          i.getPassengerBookingId().toString().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
          i.getPassengerPassportId().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
          i.getPassengerFirstName().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
          i.getPassengerLastName().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
          i.getPassengerDateOfBirth().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
          i.getPassengerSex().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
          i.getPassengerAddress().toLowerCase(Locale.getDefault()).contains(searchTerm)
      ).collect(Collectors.toList());
    }
		return passengers;
	}
}
