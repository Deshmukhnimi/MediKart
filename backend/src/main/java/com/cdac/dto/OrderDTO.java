package com.cdac.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import com.cdac.Entity.Status;

@Data // includes @Getter, @Setter, @ToString, @EqualsAndHashCode
@NoArgsConstructor // needed for ModelMapper
@AllArgsConstructor // needed for mapping or builder pattern
@Builder // allows usage of builder pattern
public class OrderDTO {

    private Long orderId;

    @NotNull(message = "User ID is required")
    private Long userId;

    private LocalDate orderDate;

    @NotNull(message = "Total amount is required")
    private Double totalAmount;

    @NotNull(message = "Order status is required")
    private Status status;
}
