package com.example.mofomas.admin;

public class DataClass {
     private String foodName;
     private String foodAmount;
     private String foodImages;
     private String foodDesc;

//These bellow are Getters:
    public String getFoodName() {
        return foodName;
    }


    public String getFoodAmount() {
        return foodAmount;
    }

    public String getFoodImages() {
        return foodImages;
    }

//Generating the constructors:
    public DataClass(String foodName, String foodAmount, String foodImages ) {
        this.foodName = foodName;
        this.foodAmount = foodAmount;
        this.foodImages = foodImages;
    }
    public DataClass()
    {

    }
}
