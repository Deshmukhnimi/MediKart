package com.cdac.dto;

import java.time.LocalDate;

import com.cdac.Entity.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicineRequestDTO {
	 private Long medicineId;
	 private String name;
	 private String description;
	 private Double price;
	 private Integer stock;
	 private String manufacturer;
	 private LocalDate expiryDate;
}
