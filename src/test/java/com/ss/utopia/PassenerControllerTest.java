package com.ss.utopia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exceptions.PassengerAlreadyExistsException;
import com.ss.utopia.exceptions.PassengerNotFoundException;
import com.ss.utopia.models.Passenger;
import com.ss.utopia.services.PassengerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(controllers = PassengerController.class)
@ActiveProfiles("Passenger Controller Test")
class PassengerControllerTest {

  private final String SERVICE_PATH_PASSENGERS = "/passengers";
  private final ObjectMapper mapper = new ObjectMapper();

  @MockBean
  private PassengerService service;

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mvc;
  private HttpHeaders headers;

  @BeforeEach
  void setup() throws Exception {
    mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    Mockito.reset(service);
    headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
  }


  // validateModel
  //=======================================================================
  @Test
  void test_validPassengerTestModel() throws Exception {
    assertEquals(Integer.valueOf(1), MOCKPassengerService.getTestPassenger().getPassengerId());
    assertEquals(Integer.valueOf(6), MOCKPassengerService.getTestPassenger().getPassengerBookingId());
    assertEquals("AFHAJKFHKAJS", MOCKPassengerService.getTestPassenger().getPassengerPassportId());
    assertEquals("FirstName1", MOCKPassengerService.getTestPassenger().getPassengerFirstName());
    assertEquals("LastName1", MOCKPassengerService.getTestPassenger().getPassengerLastName());
    assertEquals("MALE", MOCKPassengerService.getTestPassenger().getPassengerSex());
    assertEquals("1987-3-14", MOCKPassengerService.getTestPassenger().getPassengerDateOfBirth());
    assertEquals("2342 Water Lane 4291 RockCity Virginia", MOCKPassengerService.getTestPassenger().getPassengerAddress());
    assertTrue( MOCKPassengerService.getTestPassenger().getPassengerIsVeteran());
  }


  // healthCheck
  //======================================================================= 
  @Test
  void test_healthCheck_thenStatus200() throws Exception {
    when(service.findAll()).thenReturn(MOCKPassengerService.findAllWithResults());

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_PASSENGERS + "/health")
      .headers(headers)
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals("\"status\": \"up\"", response.getResponse().getContentAsString());
  }


  // findAll
  //=======================================================================
  @Test
  void test_findAllPassengers_withValidPassengers_thenStatus200() throws Exception {
    when(service.findAll()).thenReturn(MOCKPassengerService.findAllWithResults());

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_PASSENGERS)
      .headers(headers)
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(MOCKPassengerService.getTestPassengerList()), response.getResponse().getContentAsString());
  }

  @Test
  void test_findAllPassengers_withNoValidPassengers_thenStatus204() throws Exception {
    when(service.findAll()).thenReturn(MOCKPassengerService.findAllWithNoResults());

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_PASSENGERS)
      .headers(headers)
      )
      .andExpect(status().is(204))
      .andReturn();

    assertEquals("", response.getResponse().getContentAsString());
  }


  // findById
  //=======================================================================
  @Test
  void test_findById_withValidPassenger_thenStatus200() throws Exception {
    when(service.findById(1)).thenReturn(MOCKPassengerService.findById(1));

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_PASSENGERS + "/1")
      .headers(headers)
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(MOCKPassengerService.getTestPassenger()), response.getResponse().getContentAsString());
  }

  @Test
  void test_findById_withInvalidPassenger_thenStatus404() throws Exception {
    when(service.findById(-1)).thenThrow(new PassengerNotFoundException());

    mvc
      .perform(get(SERVICE_PATH_PASSENGERS + "/-1")
      .headers(headers)
      )
      .andExpect(status().is(404))
      .andReturn();
  }

  @Test
  void test_findById_withBadParams_thenStatus400() throws Exception {
    mvc
      .perform(get(SERVICE_PATH_PASSENGERS + "/NotAnInteger")
      .headers(headers)
      )
      .andExpect(status().is(400))
      .andReturn();
  }


  // findByBookingId
  //=======================================================================
  @Test
  void test_findByBookingId_withValidPassenger_thenStatus200() throws Exception {
    when(service.findByBookingId(6)).thenReturn(MOCKPassengerService.getTestPassenger());

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_PASSENGERS + "/booking/6")
      .headers(headers)
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(MOCKPassengerService.getTestPassenger()), response.getResponse().getContentAsString());
  }

  @Test
  void test_findByBookingId_withInvalidPassenger_thenStatus404() throws Exception {
    when(service.findByBookingId(-1)).thenThrow(new PassengerNotFoundException());

    mvc
      .perform(get(SERVICE_PATH_PASSENGERS + "/booking/-1")
      .headers(headers)
      )
      .andExpect(status().is(404))
      .andReturn();
  }

  @Test
  void test_findByBookingId_withBadParams_thenStatus400() throws Exception {
    mvc
      .perform(get(SERVICE_PATH_PASSENGERS + "/booking/NotAnInteger")
      .headers(headers)
      )
      .andExpect(status().is(400))
      .andReturn();
  }


  // findByPassportId
  //=======================================================================
  @Test
  void test_findByPassportId_withValidPassenger_thenStatus200() throws Exception {
    String passportId = "AFHAJKFHKAJS";
    when(service.findByPassportId(passportId)).thenReturn(MOCKPassengerService.getTestPassenger());

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_PASSENGERS + "/passport/" + passportId)
      .headers(headers)
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(MOCKPassengerService.getTestPassenger()), response.getResponse().getContentAsString());
  }

  @Test
  void test_findByPassportId_withInvalidPassenger_thenStatus404() throws Exception {
    String passportId = "NotAPassportId";
    when(service.findByPassportId(passportId)).thenThrow(new PassengerNotFoundException());

    mvc
      .perform(get(SERVICE_PATH_PASSENGERS + "/passport/" + passportId)
      .headers(headers)
      )
      .andExpect(status().is(404))
      .andReturn();
  }


  // findBySearchAndFilter
  //=======================================================================
  @Test
  void test_findBySearchAndFilter_withValidPassengers_thenStatus200() throws Exception {
    
    // An empty filterMap through findBySearchAndFilter should return all
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("searchTerms", "");

    when(service.findBySearchAndFilter(filterMap)).thenReturn(MOCKPassengerService.findAllWithResults());

    MvcResult response = mvc
      .perform(post(SERVICE_PATH_PASSENGERS + "/search")
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(MOCKPassengerService.getTestPassengerList()), response.getResponse().getContentAsString());
  }

  @Test
  void test_findBySearchAndFilter_withNoValidPassengers_thenStatus204() throws Exception {
    
    // A passengerId filter of "-1" through findBySearchAndFilter should return empty
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("passengerId", "-1");

    when(service.findBySearchAndFilter(filterMap)).thenReturn(MOCKPassengerService.findAllWithNoResults());

    MvcResult response = mvc
      .perform(post(SERVICE_PATH_PASSENGERS + "/search")
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(204))
      .andReturn();

    assertEquals("", response.getResponse().getContentAsString());
  }

  @Test
  void test_findBySearchAndFilter_withInvalidParams_thenStatus400() throws Exception {
    mvc
      .perform(post(SERVICE_PATH_PASSENGERS + "/search")
      .headers(headers)
      .content("NotAJSONObject")
      )
      .andExpect(status().is(400))
      .andReturn();
  }

  // insert
  //=======================================================================
  @Test
  void test_insert_withValidPassenger_thenStatus201() throws Exception {
    
    Passenger testPassenger = MOCKPassengerService.getTestPassenger();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("passengerBookingId", testPassenger.getPassengerBookingId().toString());
    filterMap.put("passengerPassportId", testPassenger.getPassengerPassportId());
    filterMap.put("passengerFirstName", testPassenger.getPassengerFirstName());
    filterMap.put("passengerLastName", testPassenger.getPassengerLastName());
    filterMap.put("passengerDateOfBirth", testPassenger.getPassengerDateOfBirth());
    filterMap.put("passengerSex", testPassenger.getPassengerSex());
    filterMap.put("passengerAddress", testPassenger.getPassengerAddress());
    filterMap.put("passengerIsVeteran", testPassenger.getPassengerIsVeteran().toString());

    when(service.insert(
      testPassenger.getPassengerBookingId(),
      testPassenger.getPassengerPassportId(),
      testPassenger.getPassengerFirstName(),
      testPassenger.getPassengerLastName(),
      testPassenger.getPassengerDateOfBirth(),
      testPassenger.getPassengerSex(),
      testPassenger.getPassengerAddress(),
      testPassenger.getPassengerIsVeteran()
    )).thenReturn(testPassenger);

    MvcResult response = mvc
      .perform(post(SERVICE_PATH_PASSENGERS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(201))
      .andReturn();

    assertEquals(mapper.writeValueAsString(testPassenger), response.getResponse().getContentAsString());
  }

  @Test
  void test_insert_withDuplicatePassenger_thenStatus409() throws Exception {
    
    Passenger testPassenger = MOCKPassengerService.getTestPassenger();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("passengerBookingId", testPassenger.getPassengerBookingId().toString());
    filterMap.put("passengerPassportId", testPassenger.getPassengerPassportId());
    filterMap.put("passengerFirstName", testPassenger.getPassengerFirstName());
    filterMap.put("passengerLastName", testPassenger.getPassengerLastName());
    filterMap.put("passengerDateOfBirth", testPassenger.getPassengerDateOfBirth());
    filterMap.put("passengerSex", testPassenger.getPassengerSex());
    filterMap.put("passengerAddress", testPassenger.getPassengerAddress());
    filterMap.put("passengerIsVeteran", testPassenger.getPassengerIsVeteran().toString());

    when(service.insert(
      testPassenger.getPassengerBookingId(),
      testPassenger.getPassengerPassportId(),
      testPassenger.getPassengerFirstName(),
      testPassenger.getPassengerLastName(),
      testPassenger.getPassengerDateOfBirth(),
      testPassenger.getPassengerSex(),
      testPassenger.getPassengerAddress(),
      testPassenger.getPassengerIsVeteran()
    )).thenThrow(new PassengerAlreadyExistsException());

    mvc
      .perform(post(SERVICE_PATH_PASSENGERS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(409))
      .andReturn();
  }

  @Test
  void test_insert_withInvalidPassenger_thenStatus400() throws Exception {
    
    Passenger testPassenger = MOCKPassengerService.getTestPassenger();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("passengerBookingId", "NOT_A_BOOKING_ID");
    filterMap.put("passengerPassportId", testPassenger.getPassengerPassportId());
    filterMap.put("passengerFirstName", testPassenger.getPassengerFirstName());
    filterMap.put("passengerLastName", testPassenger.getPassengerLastName());
    filterMap.put("passengerDateOfBirth", testPassenger.getPassengerDateOfBirth());
    filterMap.put("passengerSex", testPassenger.getPassengerSex());
    filterMap.put("passengerAddress", testPassenger.getPassengerAddress());
    filterMap.put("passengerIsVeteran", testPassenger.getPassengerIsVeteran().toString());

    mvc
      .perform(post(SERVICE_PATH_PASSENGERS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(400))
      .andReturn();
  }

  // update
  //=======================================================================
  @Test
  void test_update_withValidPassenger_thenStatus202() throws Exception {
    
    Passenger testPassenger = MOCKPassengerService.getTestPassenger();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("passengerId", testPassenger.getPassengerId().toString());
    filterMap.put("passengerBookingId", testPassenger.getPassengerBookingId().toString());
    filterMap.put("passengerPassportId", testPassenger.getPassengerPassportId());
    filterMap.put("passengerFirstName", testPassenger.getPassengerFirstName());
    filterMap.put("passengerLastName", testPassenger.getPassengerLastName());
    filterMap.put("passengerDateOfBirth", testPassenger.getPassengerDateOfBirth());
    filterMap.put("passengerSex", testPassenger.getPassengerSex());
    filterMap.put("passengerAddress", testPassenger.getPassengerAddress());
    filterMap.put("passengerIsVeteran", testPassenger.getPassengerIsVeteran().toString());

    when(service.update(
      testPassenger.getPassengerId(),
      testPassenger.getPassengerBookingId(),
      testPassenger.getPassengerPassportId(),
      testPassenger.getPassengerFirstName(),
      testPassenger.getPassengerLastName(),
      testPassenger.getPassengerDateOfBirth(),
      testPassenger.getPassengerSex(),
      testPassenger.getPassengerAddress(),
      testPassenger.getPassengerIsVeteran()
    )).thenReturn(testPassenger);

    MvcResult response = mvc
      .perform(put(SERVICE_PATH_PASSENGERS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(202))
      .andReturn();

    assertEquals(mapper.writeValueAsString(testPassenger), response.getResponse().getContentAsString());
  }

  @Test
  void test_update_withNonExistingPassenger_thenStatus404() throws Exception {
    
    Passenger testPassenger = MOCKPassengerService.getTestPassenger();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("passengerId", testPassenger.getPassengerId().toString());
    filterMap.put("passengerBookingId", testPassenger.getPassengerBookingId().toString());
    filterMap.put("passengerPassportId", testPassenger.getPassengerPassportId());
    filterMap.put("passengerFirstName", testPassenger.getPassengerFirstName());
    filterMap.put("passengerLastName", testPassenger.getPassengerLastName());
    filterMap.put("passengerDateOfBirth", testPassenger.getPassengerDateOfBirth());
    filterMap.put("passengerSex", testPassenger.getPassengerSex());
    filterMap.put("passengerAddress", testPassenger.getPassengerAddress());
    filterMap.put("passengerIsVeteran", testPassenger.getPassengerIsVeteran().toString());

    when(service.update(
      testPassenger.getPassengerId(),
      testPassenger.getPassengerBookingId(),
      testPassenger.getPassengerPassportId(),
      testPassenger.getPassengerFirstName(),
      testPassenger.getPassengerLastName(),
      testPassenger.getPassengerDateOfBirth(),
      testPassenger.getPassengerSex(),
      testPassenger.getPassengerAddress(),
      testPassenger.getPassengerIsVeteran()
    )).thenThrow(new PassengerNotFoundException());

    mvc
      .perform(put(SERVICE_PATH_PASSENGERS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(404))
      .andReturn();
  }

  @Test
  void test_update_withInvalidPassenger_thenStatus400() throws Exception {
    
    Passenger testPassenger = MOCKPassengerService.getTestPassenger();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("passengerBookingId", "NOT_A_BOOKING_ID");
    filterMap.put("passengerPassportId", testPassenger.getPassengerPassportId());
    filterMap.put("passengerFirstName", testPassenger.getPassengerFirstName());
    filterMap.put("passengerLastName", testPassenger.getPassengerLastName());
    filterMap.put("passengerDateOfBirth", testPassenger.getPassengerDateOfBirth());
    filterMap.put("passengerSex", testPassenger.getPassengerSex());
    filterMap.put("passengerAddress", testPassenger.getPassengerAddress());
    filterMap.put("passengerIsVeteran", testPassenger.getPassengerIsVeteran().toString());

    mvc
      .perform(put(SERVICE_PATH_PASSENGERS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(400))
      .andReturn();
  }

  // delete
  //=======================================================================
  @Test
  void test_delete_withValidPassenger_thenStatus202() throws Exception {

    mvc
      .perform(delete(SERVICE_PATH_PASSENGERS + "/1")
      .headers(headers)
      )
      .andExpect(status().is(202))
      .andReturn();
  }

  @Test
  void test_delete_withNonExistingPassenger_thenStatus404() throws Exception {
    
    when(service.delete(-1)).thenThrow(new PassengerNotFoundException());

    mvc
      .perform(delete(SERVICE_PATH_PASSENGERS + "/-1")
      .headers(headers)
      )
      .andExpect(status().is(404))
      .andReturn();
  }

  @Test
  void test_delete_withInvalidParams_thenStatus400() throws Exception {
    mvc
      .perform(delete(SERVICE_PATH_PASSENGERS + "/NOT_AN_INTEGER")
      .headers(headers)
      )
      .andExpect(status().is(400))
      .andReturn();
  }
}
