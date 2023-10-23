package com.SeedOasis.constant;

public enum CommunityCategory {

    INFORMATION("정보"),
    LIFE("일상생활"),
    FOOD("음식"),
    TRIP("여행"),
    MOVIE("영화");

    private String name;

    CommunityCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
