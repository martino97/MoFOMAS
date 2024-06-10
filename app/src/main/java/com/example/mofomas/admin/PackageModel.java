package com.example.mofomas.admin;

public class PackageModel {
    private String selectedCategory;
    private String fullName;
    private String phoneNumber;

    public PackageModel() {}

    public PackageModel(String selectedCategory, String fullName, String phoneNumber) {
        this.selectedCategory = selectedCategory;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
