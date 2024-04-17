package com.example.shopping.repository;

import com.example.shopping.entity.Company;
import com.example.shopping.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findProductByProductName(String productName);

    List<Product> findAllByProductNameContainingIgnoreCase(String productName);

    List<Product> findAllByCompanyIn(List<Company> companies);

    List<Product> findAllByProductNameContainingIgnoreCaseAndCompanyIn(String productName, List<Company> companies);

    List<Product> findAllByProductIdIn(List<Long> productIds);
}
