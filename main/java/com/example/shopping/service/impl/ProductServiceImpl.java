package com.example.shopping.service.impl;

import com.example.shopping.appConstant.LoggingConstants;
import com.example.shopping.dto.ProductDto;
import com.example.shopping.dto.ResponseDto;
import com.example.shopping.dto.UpdateProductDto;
import com.example.shopping.dto.ViewProductDto;
import com.example.shopping.entity.Company;
import com.example.shopping.entity.Product;
import com.example.shopping.exception.NoSuchUserExists;
import com.example.shopping.repository.CompanyRepository;
import com.example.shopping.repository.ProductRepository;
import com.example.shopping.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Value("${spring.application.responseCode}")
    private String responseCode;


    @Override
    public ResponseDto addProduct(ProductDto productDto) {
        var methodName = "ProductServiceImpl:addProduct";
        log.info(LoggingConstants.START_METHOD_LOG, methodName, productDto);
        Product newProduct = new Product();
        Optional<Product> product = productRepository.findProductByProductName(productDto.getProductName());
        if (product.isPresent() && product.get().getCompany().equals(productDto.getCompanyName())) {
            BeanUtils.copyProperties(product.get(), newProduct);
            newProduct.setQuantity(product.get().getQuantity() + productDto.getQuantity());

        } else {
            BeanUtils.copyProperties(productDto, newProduct, "companyName");
            Optional<Company> company = companyRepository.findCompanyByCompanyName(productDto.getCompanyName());
            if (company.isPresent()) {
                newProduct.setCompany(company.get());

            } else {
                Company newCompany = new Company();
                newCompany.setCompanyName(productDto.getCompanyName());
                newProduct.setCompany(newCompany);
            }
        }
        productRepository.save(newProduct);
        log.info(LoggingConstants.END_METHOD_LOG, methodName);
        return new ResponseDto(responseCode, "\"Product added successfully\"");
    }


    @Override
    public ResponseDto updateProduct(UpdateProductDto updateProductDto, int productId) {
        var methodName = "ProductServiceImpl:updateProduct";
        log.info(LoggingConstants.START_METHOD_LOG, methodName, updateProductDto, productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new NoSuchUserExists("Product with id " + productId + " does not exist")
                );
        BeanUtils.copyProperties(updateProductDto, product);
        productRepository.save(product);
        log.info(LoggingConstants.END_METHOD_LOG, methodName);
        return new ResponseDto(responseCode, "Product updated successfully");


    }

    @Override
    public List<ViewProductDto> getAllProducts(String productName, String companyName) {
        var methodName = "ProductServiceImpl:getAllProducts";
        log.info(LoggingConstants.START_METHOD_LOG, methodName, productName, companyName);
        List<Product> products = null;
        if (Objects.isNull(productName) && !Objects.isNull(companyName)) {
            List<Company> companies = companyRepository.findCompanyByCompanyNameContainingIgnoreCase(companyName);
            if (Boolean.FALSE.equals(companies.isEmpty())) {
                products = productRepository.findAllByCompanyIn(companies);
            }
        } else if (Objects.isNull(companyName) && !Objects.isNull(productName)) {
            products = productRepository.findAllByProductNameContainingIgnoreCase(productName);
        } else if (Objects.isNull(productName)) {
            products = productRepository.findAll();
        } else {
            List<Company> companies = companyRepository.findCompanyByCompanyNameContainingIgnoreCase(companyName);
            if (Boolean.FALSE.equals(companyName.isEmpty())) {
                products = productRepository.findAllByProductNameContainingIgnoreCaseAndCompanyIn(productName, companies);
            }
        }
        List<ViewProductDto> requiredProducts = new ArrayList<>();
        if (!Objects.isNull(products)) {
            products.forEach(product -> {
                ViewProductDto productDto = new ViewProductDto();
                BeanUtils.copyProperties(product, productDto);
                productDto.setCompanyName(product.getCompany().getCompanyName());
                requiredProducts.add(productDto);
            });
        }
        log.info(LoggingConstants.END_METHOD_LOG, methodName);

        return requiredProducts;
    }

}



