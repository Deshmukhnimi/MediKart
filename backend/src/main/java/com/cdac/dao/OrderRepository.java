package com.cdac.dao;

import com.cdac.Entity.Order;
import com.cdac.Entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
	  List<Order> findByUser(User user);
}

