package com.cdac.dto;

import com.cdac.Entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceOrderRequestDTO {


    @NotNull(message = "Payment method is required")
    private PaymentMethod method;

}
