package com.example.shopping.controller;

import com.example.shopping.dto.ResponseDto;
import com.example.shopping.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class RoleController {


    @Autowired
    private UserService userService;

    @PutMapping("/users")
    public ResponseEntity<ResponseDto> updateRole(@RequestHeader(value = "Authorization", required = false) String authToken,
                                                  @Valid @RequestParam @Email(message = "Invalid email") String username,
                                                  @Pattern(regexp = "(ADMIN)|(USER)|(EMPLOYEE)") @RequestParam String role) {
        return new ResponseEntity<>(userService.updateRole(authToken, username, role), HttpStatus.OK);
    }
}
