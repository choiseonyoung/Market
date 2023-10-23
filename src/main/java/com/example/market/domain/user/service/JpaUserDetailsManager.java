package com.example.market.domain.user.service;

import com.example.market.domain.user.entity.User;
import com.example.market.domain.user.repository.UserRepository;
import com.example.market.domain.user.entity.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class JpaUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;

    public JpaUserDetailsManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;

//        // 테스트용 user
//        createUser(CustomUserDetails.builder()
//                .username("user")
//                .password(passwordEncoder.encode("1234"))
//                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);
        return CustomUserDetails.fromEntity(optionalUser.get());
    }

    @Override
    public void createUser(UserDetails user) {
        if (this.userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        try {
            userRepository.save(((CustomUserDetails) user).newEntity());
        } catch (ClassCastException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

}
