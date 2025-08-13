package com.cdac.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CartItemDTO {

    private Long cartItemId;

    @NotNull(message = "Medicine ID is required")
    private Long medicineId;

    @NotNull(message = "Medicine name is required")
    @Size(min = 1, max = 100, message = "Medicine name must be between 1 and 100 characters")
    private String medicineName;

    @Positive(message = "Unit price must be positive")
    private double unitPrice;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @Positive(message = "Total price must be positive")
    private double totalPrice;
}
