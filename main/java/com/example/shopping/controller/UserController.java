package com.example.shopping.controller;

import com.example.shopping.appConstant.LoggingConstants;
import com.example.shopping.dto.ResponseDto;
import com.example.shopping.dto.UserDto;
import com.example.shopping.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<ResponseDto> registerUser(@Valid @RequestBody UserDto userDto) {
        var methodName = "UserController:registerUser";
        log.info(LoggingConstants.START_METHOD_LOG, methodName, userDto);
        ResponseDto response = userService.registerUser(userDto);
        log.info(LoggingConstants.END_METHOD_LOG, methodName);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{username}/role")
    public ResponseEntity<ResponseDto> updateRole(@RequestHeader("Authorization") String authToken,
                                                  @PathVariable("username") String username,
                                                  @RequestParam("role") String role) {
        ResponseDto response = userService.updateRole(authToken, username, role);
        return ResponseEntity.ok(response);
    }
}
