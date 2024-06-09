package com.example.mofomas.adapter;

public class PopularItem {
    private String foodName;
    private String foodPrice;
    private String imageUrl;

    public PopularItem(String foodName, String foodPrice, String imageUrl) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.imageUrl = imageUrl;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}