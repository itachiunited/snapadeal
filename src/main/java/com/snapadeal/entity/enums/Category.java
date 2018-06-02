package com.snapadeal.entity.enums;

public enum Category {
    FOOD_AND_BEVERAGE("1","Food & Beverage"),
    CLOTHING("2","Clothing"),
    BEAUTY_AND_SPAS("3","Beauty & Spas"),
    HEALTH_AND_FITNESS("4","Health & Fitness");

    private final String key;
    private final String value;

    Category(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }
}
