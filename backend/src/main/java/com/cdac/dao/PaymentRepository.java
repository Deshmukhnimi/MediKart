package com.cdac.dao;

import com.cdac.Entity.Order;
import com.cdac.Entity.Payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Optional<Payment> findByOrder(Order order);

	}
