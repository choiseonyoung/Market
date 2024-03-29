package com.example.market.domain.user.service;

import com.example.market.domain.user.dto.UserLoginDTO;
import com.example.market.domain.user.dto.SignupFormDTO;
import com.example.market.domain.user.entity.User;
import com.example.market.domain.user.repository.UserRepository;
import com.example.market.domain.user.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsManager {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);
        return CustomUserDetails.fromEntity(optionalUser.get());
    }

    // 회원가입
    public void signup(SignupFormDTO dto) {
        if (this.userExists(dto.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if (dto.getPassword().equals(dto.getPasswordCheck())) {
            // === 중복 검사 ===
            if (this.userExists(dto.getUsername()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            if (this.nicknameExists(dto.getNickname()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            if (this.emailExists(dto.getEmail()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            // ================

            CustomUserDetails userDetails = CustomUserDetails.builder()
                    .username(dto.getUsername())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .name(dto.getName())
                    .nickname(dto.getNickname())
                    .email(dto.getEmail())
                    .phoneNumber(dto.getPhoneNumber())
                    .gender(dto.getGender())
                    .address(dto.getAddress())
                    .build();

            User user = userDetails.newEntity();

            try {
                userRepository.save(user);
            } catch (ClassCastException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            // 비밀번호 불일치 처리
        }
    }

    @Override
    public void createUser(UserDetails user) {

    }

    public UserDetails login(UserLoginDTO dto) {
        UserDetails userDetails = this.loadUserByUsername(dto.getUsername());
        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        return userDetails;
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

    // username 중복 검사
    @Transactional(readOnly = true)
    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // 닉네임 중복 검사
    @Transactional(readOnly = true)
    public boolean nicknameExists(String nickname) {
        boolean nicknameDuplicate = userRepository.existsByNickname(nickname);
        return nicknameDuplicate;

    }

    // 이메일 중복 검사
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        boolean emailDuplicate = userRepository.existsByEmail(email);
        return emailDuplicate;
    }

}
