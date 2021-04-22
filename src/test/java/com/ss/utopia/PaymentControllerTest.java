package com.ss.utopia;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.ss.utopia.exceptions.PaymentAlreadyExistsException;
import com.ss.utopia.exceptions.PaymentNotFoundException;
import com.ss.utopia.exceptions.PaymentStatusNotFoundException;
import com.ss.utopia.models.Payment;
import com.ss.utopia.services.PaymentService;

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

@WebMvcTest(controllers = PaymentController.class)
@ActiveProfiles("Payment Controller Test")
class PaymentControllerTest {

  private final String SERVICE_PATH_PAYMENTS = "/payments";
  private final ObjectMapper mapper = new ObjectMapper();

  @MockBean
  private PaymentService service;

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
  void test_validPaymentTestModel() throws Exception {
    assertEquals(Integer.valueOf(1), MOCKPaymentService.getTestPayment().getPaymentId());
    assertEquals("fa0ff7db-c2da-40bd-ba21-1d7e81faa24a", MOCKPaymentService.getTestPayment().getPaymentBookingUuid());
    assertEquals("a2bf6dd9-b296-4e03-b1ee-55acfd1c196d", MOCKPaymentService.getTestPayment().getPaymentStripeUuid());
    assertEquals("CONFIRMED", MOCKPaymentService.getTestPayment().getPaymentStatus());
  }


  // healthCheck
  //======================================================================= 
  @Test
  void test_healthCheck_thenStatus200() throws Exception {
    when(service.findAll()).thenReturn(MOCKPaymentService.findAllWithResults());

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_PAYMENTS + "/health")
      .headers(headers)
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals("\"status\": \"up\"", response.getResponse().getContentAsString());
  }


  // findAll
  //=======================================================================
  @Test
  void test_findAllPayments_withValidPayments_thenStatus200() throws Exception {
    when(service.findAll()).thenReturn(MOCKPaymentService.findAllWithResults());

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_PAYMENTS)
      .headers(headers)
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(MOCKPaymentService.getTestPaymentList()), response.getResponse().getContentAsString());
  }

  @Test
  void test_findAllPayments_withNoValidPayments_thenStatus204() throws Exception {
    when(service.findAll()).thenReturn(MOCKPaymentService.findAllWithNoResults());

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_PAYMENTS)
      .headers(headers)
      )
      .andExpect(status().is(204))
      .andReturn();

    assertEquals("", response.getResponse().getContentAsString());
  }


  // findById
  //=======================================================================
  @Test
  void test_findById_withValidPayment_thenStatus200() throws Exception {
    when(service.findById(1)).thenReturn(MOCKPaymentService.findById(1));

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_PAYMENTS + "/1")
      .headers(headers)
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(MOCKPaymentService.getTestPayment()), response.getResponse().getContentAsString());
  }

  @Test
  void test_findById_withInvalidPayment_thenStatus404() throws Exception {
    when(service.findById(-1)).thenThrow(new PaymentNotFoundException());

    mvc
      .perform(get(SERVICE_PATH_PAYMENTS + "/-1")
      .headers(headers)
      )
      .andExpect(status().is(404))
      .andReturn();
  }

  @Test
  void test_findById_withBadParams_thenStatus400() throws Exception {
    mvc
      .perform(get(SERVICE_PATH_PAYMENTS + "/NotAnInteger")
      .headers(headers)
      )
      .andExpect(status().is(400))
      .andReturn();
  }


  // findByBookingUuid
  //=======================================================================
  @Test
  void test_findByBookingUuid_withValidPayment_thenStatus200() throws Exception {
    String bookingUuid = MOCKPaymentService.getTestPayment().getPaymentBookingUuid();
    when(service.findByBookingUuid(bookingUuid)).thenReturn(MOCKPaymentService.getTestPayment());

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_PAYMENTS + "/booking/" + bookingUuid)
      .headers(headers)
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(MOCKPaymentService.getTestPayment()), response.getResponse().getContentAsString());
  }

  @Test
  void test_findByBookingUuid_withInvalidPayment_thenStatus404() throws Exception {
    String bookingUuid = "NotValidUuid";
    when(service.findByBookingUuid(bookingUuid)).thenThrow(new PaymentNotFoundException());

    mvc
      .perform(get(SERVICE_PATH_PAYMENTS + "/booking/" + bookingUuid)
      .headers(headers)
      )
      .andExpect(status().is(404))
      .andReturn();
  }


    // findByStripeUuid
  //=======================================================================
  @Test
  void test_findByStripeUuid_withValidPayment_thenStatus200() throws Exception {
    String stripeUuid = MOCKPaymentService.getTestPayment().getPaymentStripeUuid();
    when(service.findByStripeUuid(stripeUuid)).thenReturn(MOCKPaymentService.getTestPayment());

    MvcResult response = mvc
      .perform(get(SERVICE_PATH_PAYMENTS + "/stripe/" + stripeUuid)
      .headers(headers)
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(MOCKPaymentService.getTestPayment()), response.getResponse().getContentAsString());
  }

  @Test
  void test_findByStripeUuid_withInvalidPayment_thenStatus404() throws Exception {
    String stripeUuid = "NotValidUuid";
    when(service.findByStripeUuid(stripeUuid)).thenThrow(new PaymentNotFoundException());

    mvc
      .perform(get(SERVICE_PATH_PAYMENTS + "/stripe/" + stripeUuid)
      .headers(headers)
      )
      .andExpect(status().is(404))
      .andReturn();
  }


  // findBySearchAndFilter
  //=======================================================================
  @Test
  void test_findBySearchAndFilter_withValidPayments_thenStatus200() throws Exception {
    
    // An empty filterMap through findBySearchAndFilter should return all
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("searchTerms", "");

    when(service.findBySearchAndFilter(filterMap)).thenReturn(MOCKPaymentService.findAllWithResults());

    MvcResult response = mvc
      .perform(post(SERVICE_PATH_PAYMENTS + "/search")
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(200))
      .andReturn();

    assertEquals(mapper.writeValueAsString(MOCKPaymentService.getTestPaymentList()), response.getResponse().getContentAsString());
  }

  @Test
  void test_findBySearchAndFilter_withNoValidPayments_thenStatus204() throws Exception {
    
    // A paymentId filter of "-1" through findBySearchAndFilter should return empty
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("paymentId", "-1");

    when(service.findBySearchAndFilter(filterMap)).thenReturn(MOCKPaymentService.findAllWithNoResults());

    MvcResult response = mvc
      .perform(post(SERVICE_PATH_PAYMENTS + "/search")
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
      .perform(post(SERVICE_PATH_PAYMENTS + "/search")
      .headers(headers)
      .content("NotAJSONObject")
      )
      .andExpect(status().is(400))
      .andReturn();
  }

  // insert
  //=======================================================================
  @Test
  void test_insert_withValidPayment_thenStatus201() throws Exception {
    
    Payment testPayment = MOCKPaymentService.getTestPayment();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("paymentBookingUuid", testPayment.getPaymentBookingUuid());
    filterMap.put("paymentStripeUuid", testPayment.getPaymentStripeUuid());
    filterMap.put("paymentStatus", testPayment.getPaymentStatus());

    when(service.insert(
      testPayment.getPaymentBookingUuid(),
      testPayment.getPaymentStripeUuid(),
      testPayment.getPaymentStatus()
    )).thenReturn(testPayment);

    MvcResult response = mvc
      .perform(post(SERVICE_PATH_PAYMENTS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(201))
      .andReturn();

    assertEquals(mapper.writeValueAsString(testPayment), response.getResponse().getContentAsString());
  }

  @Test
  void test_insert_withDuplicatePayment_thenStatus409() throws Exception {
    
    Payment testPayment = MOCKPaymentService.getTestPayment();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("paymentBookingUuid", testPayment.getPaymentBookingUuid());
    filterMap.put("paymentStripeUuid", testPayment.getPaymentStripeUuid());
    filterMap.put("paymentStatus", testPayment.getPaymentStatus());

    when(service.insert(
      testPayment.getPaymentBookingUuid(),
      testPayment.getPaymentStripeUuid(),
      testPayment.getPaymentStatus()
    )).thenThrow(new PaymentAlreadyExistsException());

    mvc
      .perform(post(SERVICE_PATH_PAYMENTS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(409))
      .andReturn();
  }

  @Test
  void test_insert_withInvalidStatus_thenStatus400() throws Exception {
    
    Payment testPayment = MOCKPaymentService.getTestPayment();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("paymentBookingUuid", testPayment.getPaymentBookingUuid());
    filterMap.put("paymentStripeUuid", testPayment.getPaymentStripeUuid());
    filterMap.put("paymentStatus", "NotAStatus");

    when(service.insert(
      testPayment.getPaymentBookingUuid(),
      testPayment.getPaymentStripeUuid(),
      "NotAStatus"
    )).thenThrow(new PaymentStatusNotFoundException());

    mvc
      .perform(post(SERVICE_PATH_PAYMENTS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(400))
      .andReturn();
  }

  // update
  //=======================================================================
  @Test
  void test_update_withValidPayment_thenStatus202() throws Exception {
    
    Payment testPayment = MOCKPaymentService.getTestPayment();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("paymentId", testPayment.getPaymentId().toString());
    filterMap.put("paymentBookingUuid", testPayment.getPaymentBookingUuid());
    filterMap.put("paymentStripeUuid", testPayment.getPaymentStripeUuid());
    filterMap.put("paymentStatus", testPayment.getPaymentStatus());

    when(service.update(
      testPayment.getPaymentId(),
      testPayment.getPaymentBookingUuid(),
      testPayment.getPaymentStripeUuid(),
      testPayment.getPaymentStatus()
    )).thenReturn(testPayment);

    MvcResult response = mvc
      .perform(put(SERVICE_PATH_PAYMENTS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(202))
      .andReturn();

    assertEquals(mapper.writeValueAsString(testPayment), response.getResponse().getContentAsString());
  }

  @Test
  void test_update_withNonExistingPayment_thenStatus404() throws Exception {
    
    Payment testPayment = MOCKPaymentService.getTestPayment();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("paymentId", testPayment.getPaymentId().toString());
    filterMap.put("paymentBookingUuid", "Unkown-ID");
    filterMap.put("paymentStripeUuid", testPayment.getPaymentStripeUuid());
    filterMap.put("paymentStatus", testPayment.getPaymentStatus());

    when(service.update(
      testPayment.getPaymentId(),
      "Unkown-ID",
      testPayment.getPaymentStripeUuid(),
      testPayment.getPaymentStatus()
    )).thenThrow(new PaymentNotFoundException());

    mvc
      .perform(put(SERVICE_PATH_PAYMENTS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(404))
      .andReturn();
  }

  @Test
  void test_update_withInvalidPaymentId_thenStatus400() throws Exception {
    
    Payment testPayment = MOCKPaymentService.getTestPayment();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("paymentId", "NotAnInteger");
    filterMap.put("paymentBookingUuid", testPayment.getPaymentBookingUuid());
    filterMap.put("paymentStripeUuid", testPayment.getPaymentStripeUuid());
    filterMap.put("paymentStatus", testPayment.getPaymentStatus());

    mvc
      .perform(put(SERVICE_PATH_PAYMENTS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(400))
      .andReturn();
  }

  @Test
  void test_update_withInvalidPaymentStatus_thenStatus400() throws Exception {
    
    Payment testPayment = MOCKPaymentService.getTestPayment();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("paymentId", testPayment.getPaymentId().toString());
    filterMap.put("paymentBookingUuid", testPayment.getPaymentBookingUuid());
    filterMap.put("paymentStripeUuid", testPayment.getPaymentStripeUuid());
    filterMap.put("paymentStatus", "NotAStatus");

    when(service.update(
      testPayment.getPaymentId(),
      testPayment.getPaymentBookingUuid(),
      testPayment.getPaymentStripeUuid(),
      "NotAStatus"
    )).thenThrow(new PaymentStatusNotFoundException());

    mvc
      .perform(put(SERVICE_PATH_PAYMENTS)
      .headers(headers)
      .content(mapper.writeValueAsString(filterMap))
      )
      .andExpect(status().is(400))
      .andReturn();
  }

  // delete
  //=======================================================================
  @Test
  void test_delete_withValidPayment_thenStatus202() throws Exception {

    mvc
      .perform(delete(SERVICE_PATH_PAYMENTS + "/1")
      .headers(headers)
      )
      .andExpect(status().is(202))
      .andReturn();
  }

  @Test
  void test_delete_withNonExistingPayment_thenStatus404() throws Exception {
    
    when(service.delete(-1)).thenThrow(new PaymentNotFoundException());

    mvc
      .perform(delete(SERVICE_PATH_PAYMENTS + "/-1")
      .headers(headers)
      )
      .andExpect(status().is(404))
      .andReturn();
  }

  @Test
  void test_delete_withInvalidParams_thenStatus400() throws Exception {
    mvc
      .perform(delete(SERVICE_PATH_PAYMENTS + "/NOT_AN_INTEGER")
      .headers(headers)
      )
      .andExpect(status().is(400))
      .andReturn();
  }
}
