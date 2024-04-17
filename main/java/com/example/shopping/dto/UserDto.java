package com.example.shopping.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotNull
    @Pattern(regexp = "[a-zA-Z ]+", message = "First Name should be alphabets only")
    private String firstName;

    @NotNull
    @Pattern(regexp = "[a-zA-Z ]+", message = "Last Name should be alphabets only")
    private String lastName;

    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}", message = "Email should be valid")
    private String emailId;

    @NotNull
    @Min(18)
    @Max(62)
    private int age;

    @NotNull
    @Pattern(regexp = "[6-9][0-9]{9}", message = "Contact Number should be valid")
    private String contactNumber;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "Password should be valid")
    private String password;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "Repeat Password should be valid")
    private String repeatPassword;

}
