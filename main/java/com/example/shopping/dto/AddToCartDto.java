package com.example.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartDto {

    private long productId;

    private int quantity;

    public AddToCartDto(int productId) {
        this.productId = productId;
    }
}


