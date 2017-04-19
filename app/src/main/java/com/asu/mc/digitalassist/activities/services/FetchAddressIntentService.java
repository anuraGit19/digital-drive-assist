package com.asu.mc.digitalassist.activities.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.SyncStateContract;

import com.asu.mc.digitalassist.activities.rsclient.GetLocationFromLatLong;

import java.util.List;

/**
 * Created by anurag on 4/17/17.
 */
public class FetchAddressIntentService extends IntentService{

    private GetLocationFromLatLong locationFromLatLong;
    private Double latitude;
    private Double longitude;
    protected ResultReceiver mReceiver;

    public FetchAddressIntentService(String name, Double latitude, Double longitude) {
        super(name);
        this.latitude = latitude;
        this.longitude = longitude;
    }
    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(SyncStateContract.Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        locationFromLatLong = new GetLocationFromLatLong();
        List<Address> AddrList = locationFromLatLong.getLocation(latitude, longitude, 1);

    }


}
