package com.SeedOasis.dto;

import com.SeedOasis.constant.TestCategory;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TestDto {

    @NotBlank(message = "제목을 입력주세요.")
    private String title;

    private TestCategory testCategory;

    private String content;

}
