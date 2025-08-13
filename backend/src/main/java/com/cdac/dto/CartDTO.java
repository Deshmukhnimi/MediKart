package com.cdac.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CartDTO {
    private Long cartId;

    @NotNull(message = "User ID is required")
    private Long userId;

    private double totalAmount;

    private List<CartItemDTO> items;
}
