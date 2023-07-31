package com.example.market.controller;

import com.example.market.jwt.JwtRequestDTO;
import com.example.market.jwt.JwtTokenDTO;
import com.example.market.jwt.JwtTokenUtils;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("token")
public class TokenController {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public TokenController(JwtTokenUtils jwtTokenUtils, UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    // JWT 발급
    @PostMapping("/issue")
    public JwtTokenDTO issueJwt(@RequestBody JwtRequestDTO dto) {
        UserDetails userDetails = userDetailsManager.loadUserByUsername(dto.getUsername());
        if(!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        JwtTokenDTO response = new JwtTokenDTO();
        response.setToken(jwtTokenUtils.generateToken(userDetails));
        return response;
    }
}
