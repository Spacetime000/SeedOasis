package com.SeedOasis.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewInfoDto {

    private Long id;
    private String category;
    private String title;
    private String writer;
    private LocalDateTime createTime;
    private Long views;

    public NewInfoDto() {
    }

    @Builder
    public NewInfoDto(Long id, String category, String title, String writer, LocalDateTime createTime, Long views) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.writer = writer;
        this.createTime = createTime;
        this.views = views;
    }
}
