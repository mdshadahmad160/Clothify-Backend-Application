package com.example.shopping.repository;

import com.example.shopping.entity.Cart;
import com.example.shopping.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {

    List<Payment> findAllByCartIn(List<Cart> carts);

}
