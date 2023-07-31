package com.example.market.service;

import com.example.market.dto.user.JwtTokenDTO;
import com.example.market.dto.user.UserLoginDTO;
import com.example.market.dto.user.UserSignupDTO;
import com.example.market.entity.CustomUserDetails;
import com.example.market.repository.UserRepository;
import com.example.market.util.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JpaUserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JpaUserDetailsManager manager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.manager = manager;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUp(UserSignupDTO dto) {
        if (dto.getPassword().equals(dto.getPasswordCheck())) {
            manager.createUser(CustomUserDetails.builder()
                    .username(dto.getUsername())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .phoneNumber(dto.getPhoneNumber())
                    .email(dto.getEmail())
                    .address(dto.getAddress())
                    .build());
        } else {

        }
        // 비밀번호 불일치 처리 추가 예정
    }

    public UserDetails loginPost(UserLoginDTO dto) {
        UserDetails userDetails = manager.loadUserByUsername(dto.getUsername());
        if(!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        return userDetails;
    }
}
