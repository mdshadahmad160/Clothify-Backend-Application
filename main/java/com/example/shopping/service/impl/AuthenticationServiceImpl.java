package com.example.shopping.service.impl;

import com.example.shopping.config.JwtTokenUtil;
import com.example.shopping.dto.AuthenticationRequest;
import com.example.shopping.dto.AuthenticationResponse;
import com.example.shopping.entity.User;
import com.example.shopping.exception.NoSuchUserExists;
import com.example.shopping.repository.UserRepository;
import com.example.shopping.service.AuthenticationService;
import com.example.shopping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${spring.application.responseCode}")
    private String responseCode;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User loginUser = userRepository.findUserByEmailId(request.getUsername()).orElseThrow(
                () -> new NoSuchUserExists("User with email id " + request.getUsername() + " does not exist"));
        String jwtToken = jwtTokenUtil.generateToken(loginUser, userService.getAuthority(loginUser));
        return new AuthenticationResponse(responseCode, jwtToken, "Login Successful");
    }
}
