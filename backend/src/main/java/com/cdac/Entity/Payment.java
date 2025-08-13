package com.cdac.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "payments")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    private Double amount;

    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name = "paystatus")
    private PaymentStatus paystatus;

    @Builder
    public Payment(Order order, Double amount, LocalDate paymentDate, PaymentMethod method, PaymentStatus paystatus) {
        this.order = order;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.method = method;
        this.paystatus = paystatus;
    }
}
