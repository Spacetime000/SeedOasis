package com.SeedOasis.dto;

import com.SeedOasis.constant.TestCategory;
import lombok.Data;

@Data
public class TestSearchDto {
    private TestCategory category;
    private String searchBy;
    private String searchQuery = "";
}
