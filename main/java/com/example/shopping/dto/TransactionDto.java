package com.example.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private String firstName;

    private String lastName;

    private List<CartQuantity> cartQuantities;

    private double totalPrice;

    private String paymentStatus;

    private LocalDate paymentDate;

    private String paymentType;
}
