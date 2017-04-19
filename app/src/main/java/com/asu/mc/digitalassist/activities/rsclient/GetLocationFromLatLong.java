package com.asu.mc.digitalassist.activities.rsclient;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.provider.SyncStateContract;
import android.util.Log;

import com.asu.mc.digitalassist.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by anurag on 4/17/17.
 */
public class GetLocationFromLatLong {

    private Double latitude;
    private Double longitude;
    private List<Address> addresses;
    private String errorMessage="";

    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
    public List<Address> getLocation(Double latitude, Double longitude, int i){
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
                //errorMessage = getString(R.string.no_address_found);
                errorMessage= "No address found";
                //Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(SyncStateContract.Constants.FAILURE_RESULT, errorMessage);
        }

        return addresses;
    }

}
