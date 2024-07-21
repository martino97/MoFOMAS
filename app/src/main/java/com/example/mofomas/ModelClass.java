package com.example.mofomas;
import java.util.List;

public class ModelClass {
    private List<CartItem> cartItems;
    private boolean confirmed;
    private String userId;
    private boolean isExpanded;
    private String date;

    public ModelClass() {
        // Default constructor required for Firebase
    }

    public ModelClass(List<CartItem> cartItems, boolean confirmed,String userId,String date) {
        this.cartItems = cartItems;
        this.confirmed = confirmed;
        this.isExpanded = false;
        this.userId = userId;
        this.date = date;

    }

    // Getters and Setters
    public List<CartItem> getCartItems() { return cartItems; }
    public void setCartItems(List<CartItem> cartItems) { this.cartItems = cartItems; }
    public boolean isConfirmed() { return confirmed; }
    public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }
    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {  // Add this method
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    // Inner class for CartItem
    public static class CartItem {
        private String amount;
        private String imageResource;
        private String name;
        private int quantity;


        public CartItem() {
            // Default constructor required for Firebase
        }

        public CartItem(String amount, String imageResource, String name, int quantity) {
            this.amount = amount;
            this.imageResource = imageResource;
            this.name = name;
            this.quantity = quantity;
        }

        // Getters and Setters
        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }
        public String getImageResource() { return imageResource; }
        public void setImageResource(String imageResource) { this.imageResource = imageResource; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}