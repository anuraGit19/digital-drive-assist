package com.asu.mc.digitalassist.activities;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.asu.mc.digitalassist.R;
import com.asu.mc.digitalassist.activities.model.Restaurant;
import com.asu.mc.digitalassist.activities.model.RestaurantList;
import com.asu.mc.digitalassist.activities.rsclient.GetRestaurantClient;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locManager;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        GetRestaurantClient getRestaurantClient = new GetRestaurantClient();
//        RestaurantList res = getRestaurantClient.getRestaurantList("94111", "Food");
//        Restaurant one = res.getRestaurantList().get(0);
//        Log.i("Activity", "Name: " + one.getRestName());
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}
