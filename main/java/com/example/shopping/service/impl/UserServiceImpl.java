package com.example.shopping.service.impl;

import com.example.shopping.config.JwtTokenUtil;
import com.example.shopping.dto.ResponseDto;
import com.example.shopping.dto.UserDto;
import com.example.shopping.entity.User;
import com.example.shopping.enums.Role;
import com.example.shopping.exception.InvalidUser;
import com.example.shopping.exception.NoSuchUserExists;
import com.example.shopping.exception.PasswordDoseNotMatch;
import com.example.shopping.exception.UserAlreadyExists;
import com.example.shopping.repository.UserRepository;
import com.example.shopping.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import static com.example.shopping.appConstant.AppConstants.TOKEN_PREFIX;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    @Value("${spring.application.responseCode}")
    private String responseCode;

    private final UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public ResponseDto registerUser(UserDto userDto) {
        Optional<User> user = userRepository.findUserByEmailId(userDto.getEmailId());
        if (user.isPresent()) {
            throw new UserAlreadyExists("User with email id " + userDto.getEmailId() + " already exists");
        }
        if (!userDto.getPassword().equals(userDto.getRepeatPassword())) {
            throw new PasswordDoseNotMatch("Password does not match");

        }
        User registerUser = new User();
        if (userRepository.noOfUsers() < 1) {
            registerUser.setRole(Role.ADMIN);

        } else {
            registerUser.setRole(Role.USER);
        }
        BeanUtils.copyProperties(userDto, registerUser, "repeatPassword");
        registerUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(registerUser);
        return new ResponseDto(responseCode, "User Registered Successfully");
    }

    public List<SimpleGrantedAuthority> getAuthority(User user) {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public ResponseDto updateRole(String authToken, String username, String role) {
        String jwtToken = authToken.replace(TOKEN_PREFIX, "");
        String adminUsername = jwtTokenUtil.getUsernameFromToken(jwtToken);
        if (username.equals(adminUsername)) {
            throw new InvalidUser("You cannot update your own role");
        }
        User user = userRepository.findUserByEmailId(username).orElseThrow(
                () -> new NoSuchUserExists("User with email id " + username + " does not exist"));

        Role updatedRole = Role.valueOf(role);
        user.setRole(updatedRole);
        userRepository.save(user);
        return new ResponseDto(responseCode, "Role updated successfully");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findUserByEmailId(username);
        if (!user.isPresent()) {
            throw new NoSuchUserExists("User with email id " + username + " does not exist");
        }
        return new org.springframework.security.core.userdetails.User(user.get().getEmailId(), user.get().getPassword(), getAuthority(user.get()));
    }


}
