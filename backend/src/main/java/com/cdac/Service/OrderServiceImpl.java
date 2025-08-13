package com.cdac.Service;

import com.cdac.Entity.Category;
import com.cdac.Entity.Order;
import com.cdac.Entity.OrderStatusHistory;
import com.cdac.Entity.Status;
import com.cdac.Entity.User;
import com.cdac.Exception.ResourceNotFoundException;
import com.cdac.dao.OrderRepository;
import com.cdac.dao.OrderStatusHistoryRepository;
import com.cdac.dao.UserRepository;
import com.cdac.dto.OrderDTO;

import jakarta.persistence.EntityNotFoundException;

import com.cdac.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderStatusHistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;
    
   

    //correct
    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(this::mapToOrderDTO)
                .collect(Collectors.toList());
    }
    

    // ✅ Helper method to convert Entity to DTO
    private OrderDTO mapToOrderDTO(Order order) {
        return new OrderDTO(
                order.getOrderId(),
                order.getUser().getUserId(),         // Assumes getId() exists in User
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus()
        );
    }
    
    @Override
    public void updateOrderStatus(Long orderId, Status newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Status oldStatus = order.getStatus();
        order.setStatus(newStatus);
        orderRepository.save(order);

        // Log the change
        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedAt(LocalDateTime.now())
                .build();

        historyRepository.save(history);
    }

    
    

    @Override
    public void cancelOrder(Long orderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Authorization check
        if (user.getCategory() != Category.ADMIN &&
            !order.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("You can only cancel your own orders.");
        }

        // Validate current status
        if (order.getStatus() == Status.CANCELLED || order.getStatus() == Status.DELIVERED) {
            throw new IllegalStateException("Order cannot be cancelled");
        }

        // ✅ Capture the old status before updating
        Status oldStatus = order.getStatus();

        // ✅ Update status
        order.setStatus(Status.CANCELLED);
        orderRepository.save(order);

        // ✅ Save status history
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setOldStatus(oldStatus);  // ✅ This was missing
        history.setNewStatus(Status.CANCELLED);
        history.setChangedAt(LocalDateTime.now());
        historyRepository.save(history);
    }


	


}
