package com.SeedOasis.entity;

import com.SeedOasis.constant.TestCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@DynamicInsert
@TableGenerator(
        name = "TEST_SEQ_GENERATOR",
        table = "MY_SEQUENCES",
        pkColumnValue = "TEST_SEQ",
        allocationSize = 1
)
public class Test extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TEST_SEQ_GENERATOR")
    private Long testId;

    private String title;

//    @OneToMany(mappedBy = "test")
//    private List<TestContent> testContents = new ArrayList<>();

    @Column(columnDefinition = "TINYINT", length = 1)
    @ColumnDefault("false")
    private Boolean enabled;

    @Enumerated(EnumType.STRING)
    private TestCategory testCategory;

    @Lob
    private String content;

    @ColumnDefault("0")
    private Long views;

    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<TestLike> testLikes = new ArrayList<>();

    public void addTestLike(TestLike testLike) {
        this.testLikes.add(testLike);

        if (testLike.getTest() != this) {
            testLike.setTest(this);
        }
    }


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;

//    public void addTestContent(TestContent testContent) {
//        this.testContents.add(testContent);
//
//        if (testContent.getTest() != null) {
//            testContent.setTest(this);
//        }
//    }

}
