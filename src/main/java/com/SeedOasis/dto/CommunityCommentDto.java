package com.SeedOasis.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommunityCommentDto {
    private Long id;
    private String profile;
    private String name;
    private String createBy;
    private String comment;
    private LocalDateTime createTime;

    public CommunityCommentDto() {
    }

    @Builder
    public CommunityCommentDto(Long id, String profile, String name, String createBy, String comment, LocalDateTime createTime) {
        this.id = id;
        this.profile = profile;
        this.name = name;
        this.createBy = createBy;
        this.comment = comment;
        this.createTime = createTime;
    }
}

