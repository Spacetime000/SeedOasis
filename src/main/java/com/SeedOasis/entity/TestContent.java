package com.SeedOasis.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class TestContent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long testContentId;

    @Lob
    private String question;

    @Lob
    private String answer;

    private String imgSrc;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "test_id")
//    private Test test;
//
//    public void setTest(Test test) {
//        if (this.test != null) {
//            this.test.getTestContents().remove(this);
//        }
//
//        this.test = test;
//
//        if (!test.getTestContents().contains(this)) {
//            test.getTestContents().add(this);
//        }
//    }
}
