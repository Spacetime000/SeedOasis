package com.SeedOasis.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class JoinDto {

    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "영소문자와 숫자 4~15자로 입력해주세요.")
    private String memberId;

    @Size(min = 8, message = "8자 이상으로 입력해주세요.")
    private String password;

    @Email(message = "이메일 양식에 맞춰 입력해주세요.")
    private String email;

    @Pattern(regexp = "^[a-z가-힣0-9]{2,8}$", message = "한글 또는 영소문자로 2~8자 이내로 입력해주세요.")
    private String name;
}
