package com.ss.utopia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.exceptions.PaymentAlreadyExistsException;
import com.ss.utopia.exceptions.PaymentNotFoundException;
import com.ss.utopia.exceptions.PaymentStatusNotFoundException;
import com.ss.utopia.filters.PaymentFilters;
import com.ss.utopia.models.Payment;
import com.ss.utopia.repositories.PaymentRepository;
import com.ss.utopia.services.PaymentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PaymentServiceTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @Autowired
  private PaymentService service;

  @MockBean
  private PaymentRepository paymentRepository;

  @Configuration
  @Import(PaymentService.class)
  static class TestConfig {
    @Bean
    PaymentRepository paymentRepository() {
      return mock(PaymentRepository.class);
    }
  }

  @BeforeEach
  void setup() throws Exception {
    Mockito.reset(paymentRepository);
  }

  // Validate Models
  //=======================================================================
  @Test
  void test_validPaymentTestModel() throws Exception {
    assertEquals(Integer.valueOf(1), MOCKPaymentService.getTestPayment().getPaymentId());
    assertEquals("fa0ff7db-c2da-40bd-ba21-1d7e81faa24a", MOCKPaymentService.getTestPayment().getPaymentBookingUuid());
    assertEquals("a2bf6dd9-b296-4e03-b1ee-55acfd1c196d", MOCKPaymentService.getTestPayment().getPaymentStripeUuid());
    assertEquals("CONFIRMED", MOCKPaymentService.getTestPayment().getPaymentStatus());
  }

  // findAll
  //=======================================================================
  @Test
  void test_findAll_WithValidResults() throws Exception {
    when(paymentRepository.findAll()).thenReturn(MOCKPaymentRepository.findAllWithResults());
    assertEquals(MOCKPaymentRepository.findAllWithResults(), service.findAll());
  }

  @Test
  void test_findAll_WithInvalidResults() throws Exception {
    when(paymentRepository.findAll()).thenReturn(MOCKPaymentRepository.findAllWithNoResults());
    assertEquals(MOCKPaymentRepository.findAllWithNoResults(), service.findAll());
  }

  // findById
  //=======================================================================
  @Test
  void test_findById_WithValidResults() throws Exception {
    when(paymentRepository.findById(1)).thenReturn(MOCKPaymentRepository.findById(1));
    assertEquals(MOCKPaymentRepository.getTestPaymentList().get(0), service.findById(1));
  }

  @Test
  void test_findById_WithInvalidResults() throws Exception {
    when(paymentRepository.findById(-1)).thenReturn(MOCKPaymentRepository.findById(-1));
    assertThrows(PaymentNotFoundException.class, () -> service.findById(-1));
  }

  // findByBookingUuid
  //=======================================================================
  @Test
  void test_findByBookingUuid_WithValidResults() throws Exception {
    String bookingUuid = MOCKPaymentRepository.getTestPayment().getPaymentBookingUuid();
    when(paymentRepository.findByBookingUuid(bookingUuid))
    .thenReturn(MOCKPaymentRepository.findByBookingUuid(bookingUuid));
    assertEquals(MOCKPaymentRepository.getTestPaymentList().get(0), service.findByBookingUuid(bookingUuid));
  }

  @Test
  void test_findByBookingUuid_WithInvalidResults() throws Exception {
    String bookingUuid = "NotAValidUuid";
    when(paymentRepository.findByBookingUuid(bookingUuid)).thenReturn(MOCKPaymentRepository.findByBookingUuid(bookingUuid));
    assertThrows(PaymentNotFoundException.class, () -> service.findByBookingUuid(bookingUuid));
  }

    // findByStripeUuid
  //=======================================================================
  @Test
  void test_findByStripeUuid_WithValidResults() throws Exception {
    String stripeUuid = MOCKPaymentRepository.getTestPayment().getPaymentStripeUuid();
    when(paymentRepository.findByStripeUuid(stripeUuid))
    .thenReturn(MOCKPaymentRepository.findByStripeUuid(stripeUuid));
    assertEquals(MOCKPaymentRepository.getTestPaymentList().get(0), service.findByStripeUuid(stripeUuid));
  }

  @Test
  void test_findByStripeUuid_WithInvalidResults() throws Exception {
    String stripeUuid = "NotAValidUuid";
    when(paymentRepository.findByStripeUuid(stripeUuid)).thenReturn(MOCKPaymentRepository.findByStripeUuid(stripeUuid));
    assertThrows(PaymentNotFoundException.class, () -> service.findByStripeUuid(stripeUuid));
  }

  // findBySearchAndFilter
  //=======================================================================
  @Test
  void test_findBySearchAndFilter_SearchSingle_WithResults() throws Exception {
    String searchTerm1 = "CONFIRMED";
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("searchTerms", searchTerm1);

    List<Payment> searchAndFiterResults = PaymentFilters.apply(MOCKPaymentRepository.getTestPaymentList(), filterMap);
    for(int i = 0; i < searchAndFiterResults.size(); i++) {
      String paymentAsString = mapper.writeValueAsString(searchAndFiterResults.get(i));
      assertTrue(paymentAsString.contains(searchTerm1));
    }
  }

  @Test
  void test_findBySearchAndFilter_SearchSingle_WithNoResults() throws Exception {
    String searchTerm1 = "NOT_SOMETHING_IN_PAYMENTS";
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("searchTerms", searchTerm1);

    List<Payment> searchAndFiterResults = PaymentFilters.apply(MOCKPaymentRepository.getTestPaymentList(), filterMap);
    assertEquals(0, searchAndFiterResults.size());
  }

  @Test
  void test_findBySearchAndFilter_SearchMulti_WithResults() throws Exception {
    String searchTerm1 = "CONFIRMED";
    String searchTerm2 = "40";
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("searchTerms", searchTerm1 + ", " + searchTerm2);

    List<Payment> searchAndFiterResults = PaymentFilters.apply(MOCKPaymentRepository.getTestPaymentList(), filterMap);
    for(int i = 0; i < searchAndFiterResults.size(); i++) {
      String paymentAsString = mapper.writeValueAsString(searchAndFiterResults.get(i));
      assertTrue(paymentAsString.contains(searchTerm1));
      assertTrue(paymentAsString.contains(searchTerm2));
    }
  }

  @Test
  void test_findBySearchAndFilter_SearchMulti_WithNoResults() throws Exception {
    String searchTerm1 = "CONFIRMED";
    String searchTerm2 = "PENDING"; // A payment can only have one status
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("searchTerms", searchTerm1 + ", " + searchTerm2);

    List<Payment> searchAndFiterResults = PaymentFilters.apply(MOCKPaymentRepository.getTestPaymentList(), filterMap);
    assertEquals(0, searchAndFiterResults.size());
  }

  @Test
  void test_findBySearchAndFilter_FilterPaymentID_WithResult() throws Exception {;
    
    Integer paymentId = MOCKPaymentRepository.getTestPayment().getPaymentId();
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("paymentId", paymentId.toString());

    List<Payment> searchAndFiterResults = PaymentFilters.apply(MOCKPaymentRepository.getTestPaymentList(), filterMap);
    assertEquals(1, searchAndFiterResults.size());
    assertEquals(paymentId, searchAndFiterResults.get(0).getPaymentId());
  }

  @Test
  void test_findBySearchAndFilter_FilterPaymentID_WithNoResult() throws Exception {
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("paymentId", "-1");

    List<Payment> searchAndFiterResults = PaymentFilters.apply(MOCKPaymentRepository.getTestPaymentList(), filterMap);
    assertEquals(0, searchAndFiterResults.size());
  }

  @Test
  void test_findBySearchAndFilter_FilterPaymentBookingUuid_WithResult() throws Exception {;
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("paymentBookingUuid", "fa0ff7db-c2da-40bd-ba21-1d7e81faa24a");

    List<Payment> searchAndFiterResults = PaymentFilters.apply(MOCKPaymentRepository.getTestPaymentList(), filterMap);
    assertEquals(1, searchAndFiterResults.size());
    assertEquals("fa0ff7db-c2da-40bd-ba21-1d7e81faa24a", searchAndFiterResults.get(0).getPaymentBookingUuid());
  }

  @Test
  void test_findBySearchAndFilter_FilterPaymentBookingUuid_WithNoResult() throws Exception {
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("paymentBookingUuid", "NotAValidUuid");

    List<Payment> searchAndFiterResults = PaymentFilters.apply(MOCKPaymentRepository.getTestPaymentList(), filterMap);
    assertEquals(0, searchAndFiterResults.size());
  }

  @Test
  void test_findBySearchAndFilter_FilterPaymentStripeUuid_WithResult() throws Exception {;
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("paymentStripeUuid", "b43092c3-4e61-4545-962d-4425b673dcd3");

    List<Payment> searchAndFiterResults = PaymentFilters.apply(MOCKPaymentRepository.getTestPaymentList(), filterMap);
    assertEquals(1, searchAndFiterResults.size());
    assertEquals("b43092c3-4e61-4545-962d-4425b673dcd3", searchAndFiterResults.get(0).getPaymentStripeUuid());
  }

  @Test
  void test_findBySearchAndFilter_FilterPaymentStripeUuid_WithNoResult() throws Exception {
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("paymentStripeUuid", "NotAValidUuid");

    List<Payment> searchAndFiterResults = PaymentFilters.apply(MOCKPaymentRepository.getTestPaymentList(), filterMap);
    assertEquals(0, searchAndFiterResults.size());
  }

  @Test
  void test_findBySearchAndFilter_FilterMulti_WithResult() throws Exception {;
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("paymentBookingUuid", "38d85c66-2a58-4608-9cbb-a27769b9e95f");
    filterMap.put("paymentStripeUuid", "7749569a-d956-497b-ae9c-42f1427a005f");

    List<Payment> searchAndFiterResults = PaymentFilters.apply(MOCKPaymentRepository.getTestPaymentList(), filterMap);
    assertEquals(1, searchAndFiterResults.size());
    assertEquals("38d85c66-2a58-4608-9cbb-a27769b9e95f", searchAndFiterResults.get(0).getPaymentBookingUuid());
    assertEquals("7749569a-d956-497b-ae9c-42f1427a005f", searchAndFiterResults.get(0).getPaymentStripeUuid());
  }

  @Test
  void test_findBySearchAndFilter_FilterMulti_WithNoResult() throws Exception {
    
    Map<String, String> filterMap = new HashMap<>();
    filterMap.put("paymentBookingUuid", "38d85c66-2a58-4608-9cbb-a27769b9e95f");
    filterMap.put("paymentStripeUuid", "7749569a-d956-497b-ae9c-NotAValidUuid");

    List<Payment> searchAndFiterResults = PaymentFilters.apply(MOCKPaymentRepository.getTestPaymentList(), filterMap);
    assertEquals(0, searchAndFiterResults.size());
  }

  // insert
  //=======================================================================
  @Test
  void test_insert_withValidPayment() throws Exception {
    when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment(1, "NewBookingUuid", "NewStripeUuid", "PENDING"));

    Payment expected = new Payment(1, "NewBookingUuid", "NewStripeUuid", "PENDING");
    Payment actual = service.insert("NewBookingUuid", "NewStripeUuid", "PENDING");
    assertEquals(expected.getPaymentId(), actual.getPaymentId());
    assertEquals(expected.getPaymentBookingUuid(), actual.getPaymentBookingUuid());
    assertEquals(expected.getPaymentStripeUuid(), actual.getPaymentStripeUuid());
    assertEquals(expected.getPaymentStatus(), actual.getPaymentStatus());
  }

  @Test
  void test_insert_withDuplicateStripeUuidPayment() throws Exception {
    when(paymentRepository.findByStripeUuid(MOCKPaymentRepository.getTestPayment().getPaymentStripeUuid()))
    .thenReturn(MOCKPaymentRepository.findByStripeUuid(MOCKPaymentRepository.getTestPayment().getPaymentStripeUuid()));
    assertThrows(PaymentAlreadyExistsException.class, () -> service.insert("NewBookingUuid", MOCKPaymentRepository.getTestPayment().getPaymentStripeUuid(), "PENDING"));
  }

  @Test
  void test_insert_withInvalidPaymentStatus() throws Exception {
    assertThrows(PaymentStatusNotFoundException.class, () -> service.insert("NewBookingUuid", "NewStripeUuid", "NotAValidStatus"));
  }

  // update
  //=======================================================================
  @Test
  void test_update_withValidPayment() throws Exception {
    when(paymentRepository.findById(MOCKPaymentRepository.getTestPayment().getPaymentId()))
    .thenReturn(MOCKPaymentRepository.findById(MOCKPaymentRepository.getTestPayment().getPaymentId()));
    when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment(1, "NewBookingUuid", "NewStripeUuid", "PENDING"));

    Payment expected = new Payment(1, "NewBookingUuid", "NewStripeUuid", "PENDING");
    Payment actual = service.update(1, "NewBookingUuid", "NewStripeUuid", "PENDING");
    assertEquals(expected.getPaymentId(), actual.getPaymentId());
    assertEquals(expected.getPaymentBookingUuid(), actual.getPaymentBookingUuid());
    assertEquals(expected.getPaymentStripeUuid(), actual.getPaymentStripeUuid());
    assertEquals(expected.getPaymentStatus(), actual.getPaymentStatus());
  }

  @Test
  void test_update_withDuplicateStripeUuidPayment() throws Exception {
    Integer paymentId = MOCKPaymentRepository.getTestPayment().getPaymentId();
    when(paymentRepository.findById(paymentId))
    .thenReturn(MOCKPaymentRepository.findById(paymentId));

    String paymentStripeUuid = MOCKPaymentRepository.getTestPaymentList().get(2).getPaymentStripeUuid();
    when(paymentRepository.findByStripeUuid(paymentStripeUuid))
    .thenReturn(MOCKPaymentRepository.findByStripeUuid(paymentStripeUuid));

    assertThrows(PaymentAlreadyExistsException.class, () -> service.update(1, "NewBookingUuid", paymentStripeUuid, "PENDING"));
  }

  @Test
  void test_update_withInvalidPaymentStatus() throws Exception {
    assertThrows(PaymentStatusNotFoundException.class, () -> service.update(1, "NewBookingUuid", "NewStripeUuid", "NotAValidStatus"));
  }

  // delete
  //=======================================================================
  @Test
  void test_delete_Valid() throws Exception {
    when(paymentRepository.findById(MOCKPaymentRepository.getTestPayment().getPaymentId()))
    .thenReturn(MOCKPaymentRepository.findById(MOCKPaymentRepository.getTestPayment().getPaymentId()));
    assertEquals(
      "Payment with ID: " + MOCKPaymentRepository.getTestPayment().getPaymentId() + " was deleted.", 
      service.delete(MOCKPaymentRepository.getTestPayment().getPaymentId())
    );
  }

  @Test
  void test_delete_Invalid() throws Exception {
    when(paymentRepository.findById(1)).thenReturn(Optional.empty());
    assertThrows(PaymentNotFoundException.class, () -> service.delete(1));
  }
}