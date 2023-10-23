package com.SeedOasis.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity extends BaseTimeEntity {

//    @CreatedBy
//    @ManyToOne
//    @JoinColumn(name = "createBy")
//    private Member createBy;

    @CreatedBy
    @Column(updatable = false)
    private String createBy;
}
