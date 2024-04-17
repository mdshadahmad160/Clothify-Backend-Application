package com.example.shopping.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDto {
    @NotNull
    @Pattern(regexp = "[A-Za-z ]+", message = "Product name should contains only alphabets")
    private String productName;

    @Min(1)
    private int quantity;

    @NotNull
    @Pattern(regexp = "[A-Za-z ]+", message = "Company name should contains only alphabets")
    private String companyName;

    @Min(1)
    private double price;
}
