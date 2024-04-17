package com.example.shopping.entity;


import com.example.shopping.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID userId;

    private String firstName;
    private String lastName;
    private String emailId;
    private int age;
    private String password;
    private String contactNumber;
    private Role role;
    public User(){
        this.userId = UUID.randomUUID();
    }

}
