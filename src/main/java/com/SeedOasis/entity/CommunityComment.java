package com.SeedOasis.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class CommunityComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private String comment;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void setCommunity(Community community) {

        if (this.community != null) {
            this.community.getComments().remove(this);
        }

        this.community = community;

        if (!community.getComments().contains(this)) {
            community.getComments().add(this);
        }
    }
}
