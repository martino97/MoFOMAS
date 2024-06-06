package com.example.mofomas.adapter;
public class MenuItem {
    private String name;
    private String price;
    private int imageResource;

    public MenuItem(String name, String price, int imageResource) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }
}
