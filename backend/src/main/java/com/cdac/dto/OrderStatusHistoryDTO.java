package com.cdac.dto;

import com.cdac.Entity.Status;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO to represent order status history.
 */
@Data
@NoArgsConstructor  // Public no-arg constructor (needed for ModelMapper)
@AllArgsConstructor // All-args constructor (for easier manual creation)
public class OrderStatusHistoryDTO {

    private Long id;

    private Long orderId;

    private Status oldStatus;

    private Status newStatus;

    private LocalDateTime changedAt;
}
