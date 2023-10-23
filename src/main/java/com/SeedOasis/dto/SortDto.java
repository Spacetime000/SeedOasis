package com.SeedOasis.dto;

import com.SeedOasis.constant.Direction;
import lombok.Data;


@Data
public class SortDto {
    private String target; //정렬 대상
    private Direction direction;

    public SortDto() {
    }

    public SortDto(String sort, Direction direction) {
        this.target = sort;
        this.direction = direction;
    }
}
