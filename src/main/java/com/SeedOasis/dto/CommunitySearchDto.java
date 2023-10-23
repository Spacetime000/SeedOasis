package com.SeedOasis.dto;

import com.SeedOasis.constant.CommunityCategory;
import lombok.Data;

@Data
public class CommunitySearchDto {
    private CommunityCategory communityCategory;
    private String searchBy;
    private String searchQuery = "";
}
