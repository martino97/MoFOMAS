package com.example.mofomas.adapter;


public class PopularItem {
    private String foodName;
    private String foodPrice;
    private int imageResource;

    public PopularItem(String foodName, String foodPrice, int imageResource) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.imageResource = imageResource;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public int getImageResource() {
        return imageResource;
    }
}
