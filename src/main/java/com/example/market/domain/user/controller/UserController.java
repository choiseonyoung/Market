package com.example.market.domain.user.controller;

import com.example.market.domain.user.dto.UserLoginDTO;
import com.example.market.domain.user.dto.SignupFormDTO;
import com.example.market.domain.user.service.UserService;
import com.example.market.domain.user.dto.JwtTokenDTO;
import com.example.market.global.jwt.JwtTokenUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final JwtTokenUtil jwtTokenUtil; // JWT 발급
    private final UserService userService; // 사용자 정보 확인

    // 회원가입
    @PostMapping("/signup")
    public String signup(@RequestBody @Valid SignupFormDTO dto) {
        userService.signup(dto);
        return "회원가입 성공";
    }

    // 로그인
    @PostMapping("/login")
    public JwtTokenDTO login(@RequestBody UserLoginDTO dto) {
        UserDetails userDetails = userService.login(dto);
        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();
        jwtTokenDTO.setToken(jwtTokenUtil.generateToken(userDetails));
        return jwtTokenDTO;
    }

}
