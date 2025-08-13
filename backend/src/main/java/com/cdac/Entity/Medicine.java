package com.cdac.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "medicines")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicineId;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 500)
    @Size(max = 500, message = "Description should not exceed 500 characters.")
    private String description;

    private Double price;

    private Integer stock;

    private LocalDate expiryDate;

    @Column(length = 100)
    private String manufacturer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by")
    private User addedBy;

    public Medicine(String name, String description, Double price, Integer stock,
                    LocalDate expiryDate, String manufacturer, User addedBy) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.expiryDate = expiryDate;
        this.manufacturer = manufacturer;
        this.addedBy = addedBy;
    }
}