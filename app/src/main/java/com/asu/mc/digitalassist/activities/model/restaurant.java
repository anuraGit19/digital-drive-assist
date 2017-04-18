package com.asu.mc.digitalassist.activities.model;

/**
 * Created by anurag on 4/16/17.
 */
public class Restaurant {
    private String restName;
    private String mobileUrl;
    private String ratings;
    private String contact;
    private String category;

    public Restaurant(String restName, String mobileUrl, String ratings, String contact, String category) {
        this.restName = restName;
        this.mobileUrl = mobileUrl;
        this.ratings = ratings;
        this.contact = contact;
        this.category = category;
    }

    public Restaurant() {
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getMobileUrl() {
        return mobileUrl;
    }

    public void setMobileUrl(String mobileUrl) {
        this.mobileUrl = mobileUrl;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
