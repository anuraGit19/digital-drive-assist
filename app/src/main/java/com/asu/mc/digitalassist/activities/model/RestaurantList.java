package com.asu.mc.digitalassist.activities.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 4/16/17.
 */
public class RestaurantList {
    List<Restaurant> restaurantList = new ArrayList<>();

    public RestaurantList(List<Restaurant> restaurantList) {

        this.restaurantList = restaurantList;
    }

    public RestaurantList() {

    }

    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }
}
