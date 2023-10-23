package com.SeedOasis.dto;

import com.SeedOasis.constant.CommunityCategory;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommunityFormDto {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용을 적어주세요")
    private String content;

    @NotNull(message = "목록을 선택해주세요.")
    private CommunityCategory communityCategory;

    public CommunityFormDto() {
    }

    @Builder
    public CommunityFormDto(String title, String content, CommunityCategory communityCategory) {
        this.title = title;
        this.content = content;
        this.communityCategory = communityCategory;
    }
}
