package com.example.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQuantityDto {

    private int productId;

    private int quantity;

    private double price;

    private double totalPrice;
}
