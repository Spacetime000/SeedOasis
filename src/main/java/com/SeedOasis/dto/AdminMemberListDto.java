package com.SeedOasis.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminMemberListDto {

    private String id;
    private String email;
    private String profile;
    private String name;
    private String role;
    private LocalDateTime createTime;

    public AdminMemberListDto() {
    }

    public AdminMemberListDto(String id, String email, String profile, String name, String role, LocalDateTime createTime) {
        this.id = id;
        this.email = email;
        this.profile = profile;
        this.name = name;
        this.role = role;
        this.createTime = createTime;
    }
}
