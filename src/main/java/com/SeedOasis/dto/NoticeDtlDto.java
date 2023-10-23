package com.SeedOasis.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeDtlDto {

    private Long noticeId;
    private String title;
    private String content;
    private Long views;
    private LocalDateTime createTime;

    @Builder
    public NoticeDtlDto(Long noticeId, String title, String content, Long views, LocalDateTime createTime) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.views = views;
        this.createTime = createTime;
    }
}
