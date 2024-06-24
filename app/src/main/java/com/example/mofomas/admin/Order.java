package com.example.mofomas.admin;
// Order.java
import java.util.List;

public class Order {
    private  String orderId;
    private String date;
    private String fullName;
    private String location;
    private String phoneNumber;
    private String time;
    private String userEmail;
    private String userId;
    private List<CartItem> cartItems;


    private boolean isConfirmed;

    public Order() {
        // Default constructor required for Firebase
    }



    public Order(String orderId, String date, String fullName, String location, String phoneNumber, String time,
                 String userEmail, String userId, List<CartItem> cartItems,boolean isConfirmed) {
        this.orderId = orderId;
        this.date = date;
        this.fullName = fullName;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.time = time;
        this.userEmail = userEmail;
        this.userId = userId;
        this.cartItems = cartItems;
        this.isConfirmed = isConfirmed;
    }

    // Getters and setters
    public String getOrderId() {return orderId;}

    public void setOrderId(String orderId) {this.orderId = orderId;}
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
    public boolean isConfirmed() {return isConfirmed;}

    public void setConfirmed(boolean confirmed) {isConfirmed = confirmed;}
}
