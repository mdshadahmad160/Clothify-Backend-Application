package com.example.shopping.service;


import com.example.shopping.dto.ResponseDto;
import com.example.shopping.dto.UserDto;
import com.example.shopping.entity.User;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService  extends UserDetailsService{

    ResponseDto registerUser(UserDto userDto);

    List<SimpleGrantedAuthority> getAuthority(User user);

    ResponseDto updateRole(String authToken,String username, @Pattern(regexp = "(ADMIN)|(USER)|(EMPLOYEE)") String role);



}
