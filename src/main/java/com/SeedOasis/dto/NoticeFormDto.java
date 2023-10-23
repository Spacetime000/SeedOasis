package com.SeedOasis.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NoticeFormDto {

    private Long noticeId;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    private String content;

    public NoticeFormDto() {
    }

    @Builder
    public NoticeFormDto(Long noticeId, String title, String content) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
    }
}
