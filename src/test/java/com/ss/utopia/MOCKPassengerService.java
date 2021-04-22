package com.ss.utopia;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ss.utopia.models.Passenger;

public class MOCKPassengerService {

  private static final Passenger testPassenger = new Passenger(1, 6, "AFHAJKFHKAJS", "FirstName1", "LastName1", "1987-3-14", "MALE", "2342 Water Lane 4291 RockCity Virginia", true);

  private static final Passenger[] testPassengerArray = {
    testPassenger,
    new Passenger(2, 7, "bhdjkHKKKAJS", "FirstName2", "LastName2", "1988-1-9", "MALE", "8880 Woodsman Street Marquette, MI 49855", false),
    new Passenger(3, 8, "UOIUHKJAHSAS", "FirstName3", "LastName3", "1989-7-3", "FEMALE", "530 Homestead Rd. North Miami Beach, FL 33160", false),
    new Passenger(4, 9, "WYTWHJKASFHJ", "FirstName4", "LastName4", "1988-1-9", "MALE", "75 Amherst Dr. Raleigh, NC 27603", false),
    new Passenger(5, 10, "PIPOIMBJJSSJ", "FirstName5", "LastName5", "2003-7-3", "FEMALE", "495 Henry Smith Road Rowlett, TX 75088", false),
    new Passenger(6, 11, "RTYCGZNCBCCC", "FirstName6", "LastName6", "1956-1-9", "MALE", "107 Greenrose St. Brownsburg, IN 46112", true),
    new Passenger(7, 12, "MOKJOIASJKHD", "FirstName7", "LastName7", "1972-7-3", "FEMALE", "9329 West Lakeshore St. Parkville, MD 21234", true),
    new Passenger(8, 13, "UIASHASJKKZC", "FirstName8", "LastName8", "1994-1-9", "MALE", "8109 Jefferson Drive Holland, MI 49423", false),
    new Passenger(9, 14, "QQWASNDAJSDK", "FirstName9", "LastName9", "2011-7-3", "FEMALE", "670 Gartner Dr. Shakopee, MN 55379", false),
  };


  public static Passenger getTestPassenger() {
    return testPassenger;
  }

  public static List<Passenger> getTestPassengerList() {
    return Arrays.asList(testPassengerArray);
  }

  public static List<Passenger> findAllWithResults() {
    return getTestPassengerList();
  }

  public static List<Passenger> findAllWithNoResults() {
    List<Passenger> emptyPassengerList = Arrays.asList();
    return emptyPassengerList;
  }

  public static Passenger findById(Integer id) {
    List<Passenger> passengerByIdList = getTestPassengerList().stream()
      .filter(i -> i.getPassengerId().equals(id))
      .collect(Collectors.toList());
    return !passengerByIdList.isEmpty()
      ? passengerByIdList.get(0)
      : null;
  }

  public static Passenger save(Passenger passenger) {
    return passenger;
  }

  public static String deleteById(Integer id) {
    return "Passenger with ID: " + id + " was deleted.";
  }
}