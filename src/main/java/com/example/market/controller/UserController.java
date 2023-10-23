package com.example.market.controller;

import com.example.market.dto.user.UserSignupDTO;
import com.example.market.dto.user.UserLoginDTO;
import com.example.market.dto.user.JwtTokenDTO;
import com.example.market.jwt.JwtTokenUtil;
import com.example.market.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    private final UserService userService;


    public UserController(PasswordEncoder passwordEncoder, UserDetailsManager manager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = manager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signUp() {
        return "회원가입 page";
    }

    @PostMapping("/signup")
    public String signUpRequest(@RequestBody UserSignupDTO dto) {
        userService.signUp(dto);
        return "회원가입 성공";
    }

    @GetMapping("/login")
    public String login() {
        return "로그인 page";
    }

    @PostMapping("/login")
    public JwtTokenDTO loginPost(@RequestBody UserLoginDTO dto) {
        UserDetails userDetails = userService.loginPost(dto);
        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();
        jwtTokenDTO.setToken(jwtTokenUtil.generateToken(userDetails));
        return jwtTokenDTO;
    }

}
