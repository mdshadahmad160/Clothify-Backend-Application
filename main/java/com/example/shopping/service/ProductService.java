package com.example.shopping.service;

import com.example.shopping.dto.ProductDto;
import com.example.shopping.dto.ResponseDto;
import com.example.shopping.dto.UpdateProductDto;
import com.example.shopping.dto.ViewProductDto;

import java.util.List;

public interface ProductService {

    ResponseDto addProduct(ProductDto productDto);

    ResponseDto updateProduct(UpdateProductDto updateProductDto, int productId);

    List<ViewProductDto> getAllProducts(String productName, String companyName);

}
