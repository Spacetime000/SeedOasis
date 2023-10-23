package com.SeedOasis.dto;

import com.SeedOasis.constant.CommunityCategory;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminCommunityListDto {

    private Long id;
    private CommunityCategory communityCategory;
    private String title;
    private String createBy;
    private String name;
    private LocalDateTime createTime;
    private Long views;

    public AdminCommunityListDto() {
    }

    @Builder
    public AdminCommunityListDto(Long id, CommunityCategory communityCategory, String title, String createBy, LocalDateTime createTime, Long views, String name) {
        this.id = id;
        this.communityCategory = communityCategory;
        this.title = title;
        this.createBy = createBy;
        this.createTime = createTime;
        this.views = views;
        this.name = name;
    }
}
