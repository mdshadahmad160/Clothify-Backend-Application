package com.example.shopping.repository;

import com.example.shopping.entity.Cart;
import com.example.shopping.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {


    Optional<Cart> findCartByUser(User user);

    Optional<Cart> findCartByStatusAndUser(String status, User user);

    List<Cart> findAllByUser(User user);
}

