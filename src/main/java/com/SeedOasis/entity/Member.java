package com.SeedOasis.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import javax.persistence.*;

@Entity
@Getter @Setter
@ToString
@DynamicInsert
public class Member extends BaseTimeEntity {

    @Id
    private String memberId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    @ColumnDefault("'/img/avatar.svg'")
    private String avatar;

    public Member() {
    }

    @Builder
    public Member(String memberId, String password, String email, String name, String role) {
        this.memberId = memberId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
    }
}
