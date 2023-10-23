package com.example.market.domain.user.controller;

import com.example.market.domain.user.dto.UserLoginDTO;
import com.example.market.domain.user.dto.UserSignupDTO;
import com.example.market.domain.user.service.UserService;
import com.example.market.domain.user.dto.JwtTokenDTO;
import com.example.market.global.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public String signup(@RequestBody UserSignupDTO dto) {
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
