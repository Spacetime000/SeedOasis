package com.SeedOasis.dto;

import com.SeedOasis.constant.CommunityCategory;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CommunityDto {
    private Long id;
    private CommunityCategory communityCategory;
    private String title;
    private String content;
    private String createBy;
    private String writer;
    private LocalDateTime createTime;
    private Long views;
    private String profile;

    private List<CommunityCommentDto> commentDtoList = new ArrayList<>();


    public CommunityDto() {
    }

    @Builder
    public CommunityDto(Long id, CommunityCategory communityCategory, String title, String content, String createBy, String writer, LocalDateTime createTime, Long views, String profile, List<CommunityCommentDto> commentDtoList) {
        this.id = id;
        this.communityCategory = communityCategory;
        this.title = title;
        this.content = content;
        this.createBy = createBy;
        this.writer = writer;
        this.createTime = createTime;
        this.views = views;
        this.profile = profile;
        this.commentDtoList = commentDtoList;
    }

}
