package com.SeedOasis.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@IdClass(TestLikeId.class)
@Getter @Setter
public class TestLike {

    @Id
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Id
    @ManyToOne
    @JoinColumn(name = "TEST_ID")
    private Test test;

    public void setTest(Test test) {
        if (this.test != null) {
            this.test.getTestLikes().remove(this);
        }

        this.test = test;

        if (!test.getTestLikes().contains(this)) {
            test.getTestLikes().add(this);
        }
    }
}
