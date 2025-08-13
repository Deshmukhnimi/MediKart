package com.cdac.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class AddToCartRequestDTO {
    private Long medicineId;
    private int quantity;
}
