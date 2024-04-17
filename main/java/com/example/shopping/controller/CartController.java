package com.example.shopping.controller;

import com.example.shopping.appConstant.LoggingConstants;
import com.example.shopping.dto.AddToCartDto;
import com.example.shopping.dto.ResponseDto;
import com.example.shopping.dto.ViewCartDto;
import com.example.shopping.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @PostMapping("/carts")
    public ResponseEntity<ResponseDto> addToCart(@RequestHeader(value = "Authorization", required = false) String authToken,
                                                 @RequestBody List<AddToCartDto> addToCartDtos) {
        var methodName = "CartController:addToCart";
        log.info(LoggingConstants.START_METHOD_LOG, methodName, authToken, addToCartDtos);
        ResponseDto addToCart = cartService.addToCart(authToken, addToCartDtos);
        log.info(LoggingConstants.END_METHOD_LOG, methodName);
        return new ResponseEntity<>(addToCart, HttpStatus.OK);

    }

    @GetMapping("/carts")
    public ResponseEntity<ViewCartDto> viewCart(@RequestHeader(value = "Authorization", required = false) String authToken) {
        var methodName = "CartController:viewCart";
        log.info(LoggingConstants.START_METHOD_LOG, methodName, authToken);
        ViewCartDto viewCart = cartService.viewCart(authToken);
        log.info(LoggingConstants.END_METHOD_LOG, methodName);
        return new ResponseEntity<>(viewCart, HttpStatus.OK);

    }

    @PutMapping("/carts")
    public ResponseEntity<ResponseDto> updateCart(@RequestHeader(value = "Authorization", required = false) String authToken,
                                                  @RequestBody @Valid List<AddToCartDto> addToCartDtos) {

        var methodName = "CartController:updateCart";
        log.info(LoggingConstants.START_METHOD_LOG, methodName, authToken, addToCartDtos);
        ResponseDto updatedCart = cartService.updateCart(authToken, addToCartDtos);
        log.info(LoggingConstants.END_METHOD_LOG, methodName);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);

    }

    @DeleteMapping("/cart")
    public ResponseEntity<ResponseDto> deleteCart(@RequestHeader(value = "Authorization", required = false) String authToken) {
        var methodName = "CartController:deleteCart";
        log.info(LoggingConstants.START_METHOD_LOG, methodName, authToken);
        ResponseDto deletedCart = cartService.deleteCart(authToken);
        log.info(LoggingConstants.END_METHOD_LOG, methodName);
        return new ResponseEntity<>(deletedCart, HttpStatus.OK);
    }


}