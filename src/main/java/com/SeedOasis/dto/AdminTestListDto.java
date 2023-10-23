package com.SeedOasis.dto;

import com.SeedOasis.constant.TestCategory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminTestListDto {
    private Long id;
    private String title;
    private TestCategory testCategory;
    private Long views;
    private Integer likes;
    private LocalDateTime createTime;
    private String createBy;
    private Boolean enabled;
    private String name;

    public AdminTestListDto() {
    }

    public AdminTestListDto(Long id, String title, TestCategory testCategory, Long views, Integer likes, LocalDateTime createTime, String createBy, Boolean enabled, String name) {
        this.id = id;
        this.title = title;
        this.testCategory = testCategory;
        this.views = views;
        this.likes = likes;
        this.createTime = createTime;
        this.createBy = createBy;
        this.enabled = enabled;
        this.name = name;
    }
}
