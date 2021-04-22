package com.ss.utopia.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "passenger")
public class Passenger {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Integer passengerId;

  @Column(name = "booking_id")
  private Integer passengerBookingId;

  @Column(name = "passport_id")
  private String passengerPassportId;

  @Column(name = "first_name")
  private String passengerFirstName;

  @Column(name = "last_name")
  private String passengerLastName;

  @Column(name = "date_of_birth")
  private String passengerDateOfBirth;

  @Column(name = "gender")
  private String passengerSex;

  @Column(name = "address")
  private String passengerAddress;

  @Column(name = "veteran_status")
  private Boolean passengerIsVeteran;

  public Passenger() {}

  public Passenger(
    Integer passengerBookingId,
    String passengerPassportId,
    String passengerFirstName,
    String passengerLastName,
    String passengerDateOfBirth,
    String passengerSex,
    String passengerAddress,
    Boolean passengerIsVeteran
  ) {
    this.passengerBookingId = passengerBookingId;
    this.passengerPassportId = passengerPassportId;
    this.passengerFirstName = passengerFirstName;
    this.passengerLastName = passengerLastName;
    this.passengerDateOfBirth = passengerDateOfBirth;
    this.passengerSex = passengerSex;
    this.passengerAddress = passengerAddress;
    this.passengerIsVeteran = passengerIsVeteran;
  }

  public Passenger(
    Integer passengerId,
    Integer passengerBookingId,
    String passengerPassportId,
    String passengerFirstName,
    String passengerLastName,
    String passengerDateOfBirth,
    String passengerSex,
    String passengerAddress,
    Boolean passengerIsVeteran
  ) {
    this.passengerId = passengerId;
    this.passengerBookingId = passengerBookingId;
    this.passengerPassportId = passengerPassportId;
    this.passengerFirstName = passengerFirstName;
    this.passengerLastName = passengerLastName;
    this.passengerDateOfBirth = passengerDateOfBirth;
    this.passengerSex = passengerSex;
    this.passengerAddress = passengerAddress;
    this.passengerIsVeteran = passengerIsVeteran;
  }

  public Integer getPassengerId() {
    return passengerId;
  }

  public void setPassengerId(Integer passengerId) {
    this.passengerId = passengerId;
  }

  public Integer getPassengerBookingId() {
    return this.passengerBookingId;
  }

  public void setPassengerBookingId(Integer passengerBookingId) {
    this.passengerBookingId = passengerBookingId;
  }

  public String getPassengerPassportId() {
    return passengerPassportId;
  }

  public void setPassengerPassportId(String passengerPassportId) {
    this.passengerPassportId = passengerPassportId;
  }

  public String getPassengerFirstName() {
    return passengerFirstName;
  }

  public void setPassengerFirstName(String passengerFirstName) {
    this.passengerFirstName = passengerFirstName;
  }

  public String getPassengerLastName() {
    return passengerLastName;
  }

  public void setPassengerLastName(String passengerLastName) {
    this.passengerLastName = passengerLastName;
  }

  public String getPassengerDateOfBirth() {
    return passengerDateOfBirth;
  }

  public void setPassengerDateOfBirth(String passengerDateOfBirth) {
    this.passengerDateOfBirth = passengerDateOfBirth;
  }

  public String getPassengerSex() {
    return passengerSex;
  }

  public void setPassengerSex(String passengerSex) {
    this.passengerSex = passengerSex;
  }

  public String getPassengerAddress() {
    return passengerAddress;
  }

  public void setPassengerAddress(String passengerAddress) {
    this.passengerAddress = passengerAddress;
  }

  public Boolean getPassengerIsVeteran() {
    return this.passengerIsVeteran;
  }

  public void setPassengerIsVeteran(Boolean passengerIsVeteran) {
    this.passengerIsVeteran = passengerIsVeteran;
  }
}
