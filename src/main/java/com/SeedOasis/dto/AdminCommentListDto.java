package com.SeedOasis.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminCommentListDto {

    private Long id;
    private String comment;
    private String createBy;
    private String name;
    private LocalDateTime createTime;
    private Long communityId;
    private String title;

    public AdminCommentListDto() {
    }

    public AdminCommentListDto(Long id, String comment, String createBy, String name, LocalDateTime createTime, Long communityId, String title) {
        this.id = id;
        this.comment = comment;
        this.createBy = createBy;
        this.name = name;
        this.createTime = createTime;
        this.communityId = communityId;
        this.title = title;
    }
}
