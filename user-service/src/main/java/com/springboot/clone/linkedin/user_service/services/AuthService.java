package com.springboot.clone.linkedin.user_service.services;

import com.springboot.clone.linkedin.user_service.dto.LoginRequestDto;
import com.springboot.clone.linkedin.user_service.dto.SignUpRequestDto;
import com.springboot.clone.linkedin.user_service.dto.UserDto;
import com.springboot.clone.linkedin.user_service.entity.User;
import com.springboot.clone.linkedin.user_service.exceptions.BadCredentialsException;
import com.springboot.clone.linkedin.user_service.repos.UserRepository;
import com.springboot.clone.linkedin.user_service.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserDto signUp(SignUpRequestDto signUpRequestDto) {

        User user = userRepository.findByEmail(signUpRequestDto.getEmail());

        if (user != null) {
            throw new RuntimeException("User already exists");
        }

        User newUser = new User();
        newUser.setEmail(signUpRequestDto.getEmail());
        newUser.setName(signUpRequestDto.getName());
        newUser.setPassword(PasswordUtil.hashPassword(signUpRequestDto.getPassword()));

        return modelMapper.map(userRepository.save(newUser), UserDto.class);
    }


    public String login(LoginRequestDto loginRequestDto) {

        User user = userRepository.findByEmail(loginRequestDto.getEmail());
        if (user == null) {
            throw new RuntimeException("User not found with email " + loginRequestDto.getEmail());
        }

        boolean matches = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());

        if(!matches) {
            throw new BadCredentialsException("Password is incorrect !!");
        }

        return jwtService.generateAccessToken(user);
    }

    public String getUsername(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        return user.getName();
    }

    public String getUserEmail(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        return user.getEmail();
    }
}
