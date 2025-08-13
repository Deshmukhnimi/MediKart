package com.cdac.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDTO {
    private Long categoryId;

    @NotBlank(message = "Category name is required")
    private String categoryName;
}
