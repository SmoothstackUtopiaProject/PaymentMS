package com.ss.utopia.filters;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.ss.utopia.models.Payment;

public final class PaymentFilters {

  private static final String PAYMENT_ID = "paymentId";
  private static final String PAYMENT_ID_BOOKING = "paymentBookingUuid";
  private static final String PAYMENT_ID_STRIPE = "paymentStripeUuid";
  private static final String PAYMENT_STATUS = "paymentStatus";
  private static final String SEARCH_TERMS = "searchTerms";

  private PaymentFilters() {
    throw new IllegalStateException("Utilility class 'PaymentFilters' is static and should not be instantiated.");
  }

  public static List<Payment> apply(List<Payment> payments, Map<String, String> filterMap) {
    
    // ID
		if(filterMap.keySet().contains(PAYMENT_ID)) {
			Integer parsedPaymentId = Integer.parseInt(filterMap.get(PAYMENT_ID));
			payments = PaymentFilters.filterByPaymentId(payments, parsedPaymentId);
		}

		// Booking ID
		if(filterMap.keySet().contains(PAYMENT_ID_BOOKING)) {
			payments = PaymentFilters.filterByPaymentBookingUuid(payments, filterMap.get(PAYMENT_ID_BOOKING));
		}

		// Passport ID
		if(filterMap.keySet().contains(PAYMENT_ID_STRIPE)) {
			payments = PaymentFilters.filterByPaymentStripeUuid(payments, filterMap.get(PAYMENT_ID_STRIPE));
		}

		// First Name
		if(filterMap.keySet().contains(PAYMENT_STATUS)) {
			payments = PaymentFilters.filterByPaymentStatus(payments, filterMap.get(PAYMENT_STATUS));
		}

    // SearchTerms
    if(filterMap.keySet().contains(SEARCH_TERMS)) {
      payments = PaymentFilters.filterBySearchTerms(payments, filterMap.get(SEARCH_TERMS));
    }

		return payments;
  }

  public static List<Payment> filterByPaymentId 
  (Collection<Payment> payments, Integer paymentId) {
    return payments.parallelStream()
      .filter(i -> i.getPaymentId().equals(paymentId))
      .collect(Collectors.toList());
  }

  public static List<Payment> filterByPaymentBookingUuid
  (Collection<Payment> payments, String paymentBookingUuid) {
    return payments.parallelStream()
      .filter(i -> i.getPaymentBookingUuid().equals(paymentBookingUuid))
      .collect(Collectors.toList());
  }

  public static List<Payment> filterByPaymentStripeUuid
  (Collection<Payment> payments, String paymentStripeUuid) {
    return payments.parallelStream()
      .filter(i -> i.getPaymentStripeUuid().equals(paymentStripeUuid))
      .collect(Collectors.toList());
  }

  public static List<Payment> filterByPaymentStatus 
  (Collection<Payment> payments, String paymentStatus) {
    return payments.parallelStream()
      .filter(i -> i.getPaymentStatus().equals(paymentStatus))
      .collect(Collectors.toList());
  }

  public static List<Payment> filterBySearchTerms(List<Payment> payments, String searchTerms) {
    String formattedSearch = searchTerms.toLowerCase(Locale.getDefault()).replace(", ", ",");
    String[] splitTerms = formattedSearch.split(",");

    for(String searchTerm : splitTerms) {
      payments = payments.parallelStream()
      .filter((Payment i) ->
          i.getPaymentId().toString().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
          i.getPaymentBookingUuid().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
          i.getPaymentStripeUuid().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
          i.getPaymentStatus().toLowerCase(Locale.getDefault()).contains(searchTerm)
      ).collect(Collectors.toList());
    }
		return payments;
	}
}
