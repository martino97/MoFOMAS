package com.example.mofomas.adapter;
public class CartItem {
    private String name;
    private int quantity;
    private int imageResource;

    public CartItem(String name, int quantity, int imageResource) {
        this.name = name;
        this.quantity = quantity;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
