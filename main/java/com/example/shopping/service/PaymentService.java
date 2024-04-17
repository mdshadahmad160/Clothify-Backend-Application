package com.example.shopping.service;

import com.example.shopping.dto.ResponseDto;
import com.example.shopping.dto.TransactionDto;

import java.util.List;

public interface PaymentService {

    ResponseDto makePayment(String authToken,String walletId);
    List<TransactionDto> getTransactions(String authToken);
}
