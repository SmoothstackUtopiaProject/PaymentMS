package com.ss.utopia.repositories;

import java.util.List;
import java.util.Optional;

import com.ss.utopia.models.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

  @Query(value = "SELECT * FROM payments WHERE booking_uuid = ?1", nativeQuery = true)
  Optional<Payment> findByBookingUuid(String bookingUuid);

  @Query(value = "SELECT * FROM payments WHERE stripe_uuid = ?1", nativeQuery = true)
  Optional<Payment> findByStripeUuid(String stripeUuid);

  @Query(value = "SELECT * FROM payments WHERE status = ?1", nativeQuery = true)
  List<Payment> findAllWithStatus(String status);
}
