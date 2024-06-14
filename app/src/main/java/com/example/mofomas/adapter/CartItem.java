package com.example.mofomas.adapter;
public class CartItem {
    private String name;

    private String amount;
    private int quantity;
    private String imageResource;

    public CartItem(String name,String amount, String imageResource, int quantity) {
        this.name = name;
        this.amount = amount;
        this.quantity = 1;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {this.name = name;}
    public String getAmount() {return amount;}
    public void setAmount(String amount) {this.amount = amount;}
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }
}
