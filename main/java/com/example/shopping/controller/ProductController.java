package com.example.shopping.controller;

import com.example.shopping.appConstant.LoggingConstants;
import com.example.shopping.dto.ProductDto;
import com.example.shopping.dto.ResponseDto;
import com.example.shopping.dto.UpdateProductDto;
import com.example.shopping.dto.ViewProductDto;
import com.example.shopping.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Shad Ahmad
 */

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/internal/products")
    public ResponseEntity<ResponseDto> addProduct(@Valid @RequestBody ProductDto productDto) {

        var methodName = "ProductController:addProduct";
        log.info(LoggingConstants.START_METHOD_LOG, methodName, productDto);

        ResponseDto savedProduct = productService.addProduct(productDto);
        log.info(LoggingConstants.END_METHOD_LOG, methodName);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/internal/products/{productId}")
    public ResponseEntity<ResponseDto> updateProduct(@Valid @RequestBody UpdateProductDto updateProductDto,
                                                     @PathVariable("productId") int productId) {
        var methodName = "ProductController:updateProduct";
        log.info(LoggingConstants.START_METHOD_LOG, methodName, updateProductDto, productId);
        ResponseDto updatedProduct = productService.updateProduct(updateProductDto, productId);
        log.info(LoggingConstants.END_METHOD_LOG, methodName);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

    }

    @GetMapping("/products")
    public ResponseEntity<List<ViewProductDto>> getAllProducts(@RequestParam(required = false) String productName,
                                                               @RequestParam(required = false) String companyName) {
        var methodName = "ProductController:getAllProducts";
        log.info(LoggingConstants.START_METHOD_LOG, methodName, productName, companyName);
        List<ViewProductDto> viewProductDto = productService.getAllProducts(productName, companyName);
        log.info(LoggingConstants.END_METHOD_LOG, methodName);
        return new ResponseEntity<>(viewProductDto, HttpStatus.OK);

    }


}
