package com.ss.utopia;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ss.utopia.models.Payment;

public class MOCKPaymentService {

  private static final Payment testPayment = new Payment(1, "fa0ff7db-c2da-40bd-ba21-1d7e81faa24a", "a2bf6dd9-b296-4e03-b1ee-55acfd1c196d", "CONFIRMED");

  private static final Payment[] testPaymentArray = {
    testPayment,
    new Payment(2, "805eb045-d3fd-456c-8f07-d07af6ef7baf", "42b99e9a-e2d4-409c-a273-dd539c7e865a", "CONFIRMED"),
    new Payment(3, "f52e2558-eebd-4e04-bd74-60a4f8f48053", "2fb2a531-b69a-4067-a6b2-e8861945525a", "CONFIRMED"),
    new Payment(4, "973bd42d-2c14-4853-9aa1-5e0a60eee1e1", "29919f66-b740-4380-9dd9-6cf7a538ec2a", "CONFIRMED"),
    new Payment(5, "75db2444-51f1-46c9-beb0-7565d3e0a217", "c5c55359-a7ac-4d88-a754-56dd7e909d01", "PENDING"),
    new Payment(6, "f29fca39-8ce0-4c84-9519-23795c8dc661", "b43092c3-4e61-4545-962d-4425b673dcd3", "PENDING"),
    new Payment(7, "79abad2a-baa3-4b2a-9371-6a4e63c76213", "6ea55da2-6324-4833-a0c7-46f37c594b4f", "PENDING"),
    new Payment(8, "ec980a29-4903-4075-a900-cb688cf8ffa5", "2516f0a1-96b0-4457-b978-9aecd9e661d2", "PENDING"),
    new Payment(9, "38d85c66-2a58-4608-9cbb-a27769b9e95f", "7749569a-d956-497b-ae9c-42f1427a005f", "REJECTED"),
  };


  public static Payment getTestPayment() {
    return testPayment;
  }

  public static List<Payment> getTestPaymentList() {
    return Arrays.asList(testPaymentArray);
  }

  public static List<Payment> findAllWithResults() {
    return getTestPaymentList();
  }

  public static List<Payment> findAllWithNoResults() {
    List<Payment> emptyPaymentList = Arrays.asList();
    return emptyPaymentList;
  }

  public static Payment findById(Integer id) {
    List<Payment> paymentList = getTestPaymentList().stream()
      .filter(i -> i.getPaymentId().equals(id))
      .collect(Collectors.toList());
    return !paymentList.isEmpty()
      ? paymentList.get(0)
      : null;
  }

  public static Payment findByBookingUuid(String bookingUuid) {
    List<Payment> paymentList = getTestPaymentList().stream()
      .filter(i -> i.getPaymentBookingUuid().equals(bookingUuid))
      .collect(Collectors.toList());
    return !paymentList.isEmpty()
      ? paymentList.get(0)
      : null;
  }

  public static Payment findByStripeUuid(String stripeUuid) {
    List<Payment> paymentList = getTestPaymentList().stream()
      .filter(i -> i.getPaymentStripeUuid().equals(stripeUuid))
      .collect(Collectors.toList());
    return !paymentList.isEmpty()
      ? paymentList.get(0)
      : null;
  }

  public static Payment save(Payment payment) {
    return payment;
  }

  public static String deleteById(Integer paymentId) {
    return "Payment with ID: " + paymentId + " was deleted.";
  }
}