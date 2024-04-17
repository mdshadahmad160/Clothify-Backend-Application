package com.example.shopping.service;

import com.example.shopping.dto.ResponseDto;

public interface WalletService {

    ResponseDto addWallet(String authToken,String walletType,double walletBalance);
}
