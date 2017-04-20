package com.asu.mc.digitalassist.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.asu.mc.digitalassist.R;
import com.asu.mc.digitalassist.activities.models.Restaurant;
import com.asu.mc.digitalassist.activities.rsclient.RestaurantApiClient;
import com.asu.mc.digitalassist.activities.services.NotificationService;
import com.asu.mc.digitalassist.activities.utility.RestaurantListAdapter;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.asu.mc.digitalassist.activities.services.FetchAddressIntentService;
import com.asu.mc.digitalassist.activities.utility.Constants;
import java.util.List;

public class RestaurantActivity extends ListActivity implements OnConnectionFailedListener, ConnectionCallbacks {

    protected static final String TAG = RestaurantActivity.class.getSimpleName();

    protected GoogleApiClient mGoogleApiClient;
    protected RestaurantApiClient mRestaurantApiClient;

    protected TextView txtLat;
    protected TextView txtLong;
    protected TextView txtRestName;

    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    protected boolean mAddressRequested;
    protected String mAddressOutput;
    protected ListView restaurantListView;

    protected final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1;
    /*For notification*/
    private static IdpResponse idpResponse = null;
    public static Intent createIntent(Context context,IdpResponse response){
        Intent intent = new Intent(context,RestaurantActivity.class);
        idpResponse = response;
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

//        txtLat = (TextView) findViewById(R.id.txtGpsLatitude);
//        txtLong = (TextView) findViewById(R.id.txtGpsLongitude);
//        txtRestName = (TextView) findViewById(R.id.txtRestaurantName);

//        checkAppPermissions();
//        buildGoogleApiClient();

        new FetchRestaurantTask().execute("85281");
        mResultReceiver = new AddressResultReceiver(new Handler());
        startAddressIntentService();
        startService(NotificationService.createIntentOverSpeedNotificationService(getApplicationContext()));

    }

    protected void startAddressIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

//    public void fetchAddressButtonHandler(View view) {
//        // Only start the service to fetch the address if GoogleApiClient is
//        // connected.
//        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
//            startIntentService();
//        }
//        // If GoogleApiClient isn't connected, process the user's request by
//        // setting mAddressRequested to true. Later, when GoogleApiClient connects,
//        // launch the service to fetch the address. As far as the user is
//        // concerned, pressing the Fetch Address button
//        // immediately kicks off the process of getting the address.
//        mAddressRequested = true;
//        updateUIWidgets();
//    }

    private class FetchRestaurantTask extends AsyncTask<String, Void, List<Restaurant>> {

        @Override
        protected List<Restaurant> doInBackground(String... strings) {
            String zipCodeOrCityName = strings[0];
            mRestaurantApiClient = new RestaurantApiClient();
            List<Restaurant> restaurantList = mRestaurantApiClient.getNearbyRestaurantList(zipCodeOrCityName);
            return restaurantList;
        }

        protected void onPostExecute(List<Restaurant> restaurantList) {
            if (restaurantList != null && restaurantList.size() > 0) {
                ArrayAdapter<Restaurant> restaurantArrayAdapter = new RestaurantListAdapter(RestaurantActivity.this, restaurantList);
                setListAdapter(restaurantArrayAdapter);
            } else {
                Toast.makeText(getApplicationContext(), "No restaurants for the current location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            Log.d(TAG, "Building Google API Client");

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void checkAppPermissions() {
        Log.d(TAG, "Inside checkAppPermissions");

        if (ActivityCompat.checkSelfPermission(RestaurantActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "App should show an explanation");
                // TODO: No explanation added yet

            } else {
                Log.d(TAG, "Requesting permission from user");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_LOCATION);
            }
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent moveToWebView = new Intent(this, RestaurantWebViewActivity.class);
        Restaurant res = (Restaurant) getListAdapter().getItem(position);
        String url = res.getMobileUrl();
        moveToWebView.putExtra("EXTRA_URL", url);
        startActivity(moveToWebView);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "Inside On Start");
        super.onStart();

//        mGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Inside On Destroy");
        super.onDestroy();

//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Inside On onConnectionFailed");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "Inside OnConnected");

//        checkAppPermissions();
//        String latLong = LocationUtility.getCurrentKnownLocation(mGoogleApiClient);
//        String[] coordinates = latLong.split(",");
//        txtLat.setText(coordinates[0]);
//        txtLong.setText(coordinates[1]);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                Toast.makeText(this, "No GeoCoder available",
                        Toast.LENGTH_LONG).show();
                return;
            }
            //if (mAddressRequested) {
            startAddressIntentService();
            //}
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_LOCATION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Location access permission GRANTED");
                } else {
                    Log.i(TAG, "Location access permission REJECTED");
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Inside On onConnectionSuspended");
//        mGoogleApiClient.connect();
    }

    @SuppressLint("ParcelCreator")
    private class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            // Address Update on UI - displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                Log.i(TAG, "address - Found ");
                //Toast.makeText(this, "dkbajs", Toast.LENGTH_SHORT).show();
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            //mAddressRequested = false;
            //updateUIWidgets();
        }
    }

}
