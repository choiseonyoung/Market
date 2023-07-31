package com.example.market.controller;

import com.example.market.entity.CustomUserDetails;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;


    public UserController(PasswordEncoder passwordEncoder, UserDetailsManager manager) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = manager;
    }

    @GetMapping("/signup")
    public String signUp() {
        return "회원가입 page";
    }

    @PostMapping("/signup")
    public String signUpRequest(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("password-check") String passwordCheck) {
        if (password.equals(passwordCheck)) {
            log.info("패스워드가 일치합니다!");

            userDetailsManager.createUser(CustomUserDetails.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .build());
            return "회원가입 성공";
        }
        log.warn("패스워드 불일치");
        return "회원가입 실패";
    }

    @GetMapping("/login")
    public String login() {
        return "로그인 page";
    }


}
