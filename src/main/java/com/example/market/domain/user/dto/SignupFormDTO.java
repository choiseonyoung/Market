package com.example.market.domain.user.dto;

import com.example.market.domain.user.entity.Gender;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupFormDTO {

    @NotBlank(message = "아이디를 입력해주세요")
    @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영어 소문자와 숫자만 사용 가능하며 4자 이상 20자 이하로 입력해주세요")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$", message = "영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호를 한번 더 입력해주세요")
    private String passwordCheck;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$" , message = "닉네임은 특수문자 사용이 불가하며, 2자 이상 10자 이하로 입력해주세요")
    private String nickname;

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "휴대폰 번호를 입력해주세요")
    private String phoneNumber;

    @NotNull
    private Gender gender;

    private String address;

    @Builder
    public SignupFormDTO(String username, String password, String passwordCheck, String name, String nickname, String email, String phoneNumber, Gender gender, String address) {
        this.username = username;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.address = address;
    }
}
