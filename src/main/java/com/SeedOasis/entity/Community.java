package com.SeedOasis.entity;

import com.SeedOasis.constant.CommunityCategory;
import lombok.Builder;
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
        name = "COMMUNITY_SEQ_GENERATOR",
        table = "MY_SEQUENCES",
        pkColumnValue = "COMMUNITY_SEQ",
        allocationSize = 1
)
public class Community extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COMMUNITY_SEQ_GENERATOR")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CommunityCategory communityCategory;

    private String title;

    @Lob
    private String content;

    @ColumnDefault("0")
    private Long views;

    @OneToMany(mappedBy = "community", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CommunityComment> comments = new ArrayList<>();

    public void addComment(CommunityComment communityComment) {
        this.comments.add(communityComment);

        if (communityComment.getCommunity() != this) {
            communityComment.setCommunity(this);
        }
    }

    public Community() {
    }

    @Builder
    public Community(CommunityCategory communityCategory, String title, String content) {
        this.communityCategory = communityCategory;
        this.title = title;
        this.content = content;
    }
}
