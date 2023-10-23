package com.SeedOasis.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter @Setter
@ToString
@DynamicInsert
@TableGenerator(
        name = "NOTICE_SEQ_GENERATOR",
        table = "MY_SEQUENCES",
        pkColumnValue = "NOTICE_SEQ",
        allocationSize = 1
)
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "NOTICE_SEQ_GENERATOR")
    private Long noticeId;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    @ColumnDefault("0")
    private Long views;

    public Notice() {
    }

    @Builder
    public Notice(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
