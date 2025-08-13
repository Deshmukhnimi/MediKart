package com.cdac.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.Entity.Order;
import com.cdac.Entity.OrderStatusHistory;
import com.cdac.Entity.User;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long>{

	List<OrderStatusHistory> findByOrder(Order order);

	List<OrderStatusHistory> findByOrderOrderId(Long orderId);


}
