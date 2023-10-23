package com.example.market.domain.user.service;

import com.example.market.domain.user.dto.UserLoginDTO;
import com.example.market.domain.user.dto.UserSignupDTO;
import com.example.market.domain.user.repository.UserRepository;
import com.example.market.domain.user.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JpaUserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;

    public void signup(UserSignupDTO dto) {
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

    public UserDetails login(UserLoginDTO dto) {
        UserDetails userDetails = manager.loadUserByUsername(dto.getUsername());
        if(!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        return userDetails;
    }
}
