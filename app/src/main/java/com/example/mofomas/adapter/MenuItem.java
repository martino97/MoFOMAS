package com.example.mofomas.adapter;
public class MenuItem {
    private String foodName;
    private String foodAmount;
    private String foodImages;

    public MenuItem(String foodName, String foodAmount, String foodImages) {
        this.foodName = foodName;
        this.foodAmount = foodAmount;
        this.foodImages = foodImages;
    }

    public String getName() {
        return foodName;
    }

    public String getPrice() {
        return foodAmount;
    }

    public String getImageUrl() {
        return foodImages;
    }
}