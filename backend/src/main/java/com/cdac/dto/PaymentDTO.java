package com.cdac.dto;

import com.cdac.Entity.PaymentMethod;
import com.cdac.Entity.PaymentStatus; // ✅ Make sure this is in enums package
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PaymentDTO {

    private Long paymentId;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Amount is required")
    private Double amount;

    private LocalDate paymentDate;

    @NotNull(message = "Payment method is required")
    private PaymentMethod method;  // ✅ changed from String to enum

    @NotNull(message = "Payment status is required")
    private PaymentStatus paystatus;

    public PaymentDTO(Long paymentId, Long orderId, Double amount,
                      LocalDate paymentDate, PaymentMethod method, PaymentStatus paystatus) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.method = method;
        this.paystatus = paystatus;
    }
}
