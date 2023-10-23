package com.SeedOasis.dto;

import com.SeedOasis.constant.TestCategory;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestPageDto {

    private Long id;
    private String title;
    private TestCategory category;
    private long views;
    private long like;
    private LocalDateTime createTime;
    private String createBy;

    private boolean enabled;

    public TestPageDto() {
    }

    @Builder
    public TestPageDto(Long id, String title, TestCategory category, long views, long like, LocalDateTime createTime, String createBy, boolean enabled) {
        this.id = id;
        this.category = category;
        this.views = views;
        this.like = like;
        this.createTime = createTime;
        this.createBy = createBy;
        this.title = title;
        this.enabled = enabled;
    }
}
