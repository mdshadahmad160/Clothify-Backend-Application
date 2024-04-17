package com.example.shopping.repository;

import com.example.shopping.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findUserByEmailId(String emailId);

    @Query(value = "SELECT COUNT(u) FROM User u")
    int noOfUsers();
}
