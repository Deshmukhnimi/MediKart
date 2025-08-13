package com.cdac.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_status_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 30)
    private Status oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 30)
    private Status newStatus;

    @Column(nullable = false)
    private LocalDateTime changedAt;

   
}

