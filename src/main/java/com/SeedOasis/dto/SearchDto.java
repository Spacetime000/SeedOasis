package com.SeedOasis.dto;

import lombok.Data;

@Data
public class SearchDto {
    private String searchBy;
    private String searchQuery = "";
}
