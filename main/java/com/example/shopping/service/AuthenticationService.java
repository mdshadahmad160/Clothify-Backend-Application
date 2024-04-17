package com.example.shopping.service;

import com.example.shopping.dto.AuthenticationRequest;
import com.example.shopping.dto.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest request);
}

