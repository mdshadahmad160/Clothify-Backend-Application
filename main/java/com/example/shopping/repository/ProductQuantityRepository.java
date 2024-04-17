package com.example.shopping.repository;

import com.example.shopping.entity.Cart;
import com.example.shopping.entity.ProductQuantity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductQuantityRepository extends JpaRepository<ProductQuantity,Integer> {

    List<ProductQuantity> findAllByCart(Cart cart);

    List<ProductQuantity> findAllByCartIn(List<Cart> carts);
}
