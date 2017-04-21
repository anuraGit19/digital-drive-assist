package com.asu.mc.digitalassist.activities.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.asu.mc.digitalassist.R;
import com.asu.mc.digitalassist.activities.utility.Constants;

import java.util.List;
import java.util.Locale;

/**
 * Created by anurag on 4/17/17.
 */
public class FetchAddressIntentService extends IntentService{

    private static final String TAG = "FetchAddressIS";
    private Double latitude;
    private Double longitude;
    protected ResultReceiver mReceiver;

    public FetchAddressIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    public FetchAddressIntentService(String name, Double latitude, Double longitude) {
        super(name);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";
        List<Address> addresses = null;

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            }catch(Exception e){
                e.printStackTrace();
            }
            if (addresses == null || addresses.size()  == 0) {
                if (errorMessage.isEmpty()) {
                    errorMessage= "No address found";
                    Log.e(TAG, errorMessage);
                }
                deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
            }


       // locationFromLatLong = new GetLocationFromLatLong();
       // List<Address> AddrList = locationFromLatLong.getLocation(latitude, longitude, 1);


        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        if (mReceiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
            return;
        }
        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        if (location == null) {
            errorMessage = getString(R.string.no_location_data_provided);
            Log.wtf(TAG, errorMessage);
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);

        }
    }
        private void deliverResultToReceiver(int resultCode, String message) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.RESULT_DATA_KEY, message);
            mReceiver.send(resultCode, bundle);
        }


}
