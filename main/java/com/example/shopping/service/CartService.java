package com.example.shopping.service;

import com.example.shopping.dto.AddToCartDto;
import com.example.shopping.dto.ResponseDto;
import com.example.shopping.dto.ViewCartDto;
import java.util.List;

public interface CartService {

    ResponseDto addToCart(String authToken, List<AddToCartDto> addToCartDtos);

    ViewCartDto viewCart(String authToken);

    ResponseDto updateCart(String authToken, List<AddToCartDto> addToCartDtos);

    ResponseDto deleteCart(String authToken);
}
