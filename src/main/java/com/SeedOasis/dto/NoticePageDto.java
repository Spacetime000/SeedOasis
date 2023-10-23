package com.SeedOasis.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticePageDto {
    private Long noticeId;
    private String title;
    private LocalDateTime createTime;
    private Long views;

    public NoticePageDto() {
    }

    @Builder
    public NoticePageDto(Long noticeId, String title, LocalDateTime createTime, Long views) {
        this.noticeId = noticeId;
        this.title = title;
        this.createTime = createTime;
        this.views = views;
    }
}
