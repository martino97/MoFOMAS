package com.example.mofomas.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class PopularItem implements Parcelable {
    private String foodName;
    private String foodPrice;
    private String imageUrl;

    public PopularItem(String foodName, String foodPrice, String imageUrl) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.imageUrl = imageUrl;
    }

    protected PopularItem(Parcel in) {
        foodName = in.readString();
        foodPrice = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<PopularItem> CREATOR = new Creator<PopularItem>() {
        @Override
        public PopularItem createFromParcel(Parcel in) {
            return new PopularItem(in);
        }

        @Override
        public PopularItem[] newArray(int size) {
            return new PopularItem[size];
        }
    };

    public String getFoodName() {
        return foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(foodName);
        parcel.writeString(foodPrice);
        parcel.writeString(imageUrl);
    }
}