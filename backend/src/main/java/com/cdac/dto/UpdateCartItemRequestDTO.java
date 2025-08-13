package com.cdac.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCartItemRequestDTO {
    private Long cartItemId;
    @JsonProperty("quantity")
    private int newQuantity;
}
