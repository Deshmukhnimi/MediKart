package com.cdac.dto;

import java.time.LocalDate;

import com.cdac.Entity.Medicine;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MedicineDTO {
    private Long medicineId;

    @NotBlank(message = "Medicine name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be at least 1")
    private Double price;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;
    
    public MedicineDTO(Medicine medicine) {
        this.medicineId = medicine.getMedicineId();
        this.name = medicine.getName();
        this.description = medicine.getDescription();
        this.price = medicine.getPrice();
        this.stock = medicine.getStock();
        this.manufacturer = medicine.getManufacturer();
        this.expiryDate = medicine.getExpiryDate();
    }

}
    
