package com.SeedOasis.constant;

public enum TestCategory {
    CERTIFICATION("자격증"),
    MATH("수학"),
    PHYSICS("물리"),
    HISTORY("역사"),
    ETC("기타");

    private final String category;

    TestCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

}
