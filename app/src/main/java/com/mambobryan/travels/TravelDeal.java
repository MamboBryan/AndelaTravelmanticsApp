package com.mambobryan.travels;

import java.io.Serializable;

public class TravelDeal implements Serializable {

    private String mTitle;
    private String mDescription;
    private String mPrice;
    private String mImageUrl;


    private String mImageName;
    private String mId;

    public TravelDeal() {
    }

    public TravelDeal(String title, String price, String description,
                      String imageUrl, String imageName) {
        mTitle = title;
        mDescription = description;
        mPrice = price;
        mImageUrl = imageUrl;
        mImageName = imageName;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public void setImageName(String imageName) {
        mImageName = imageName;
    }


    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getPrice() {
        return mPrice;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getImageName() {
        return mImageName;
    }


}
