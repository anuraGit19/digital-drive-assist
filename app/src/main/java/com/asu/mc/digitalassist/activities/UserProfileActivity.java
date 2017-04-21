package com.asu.mc.digitalassist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.asu.mc.digitalassist.R;

public class UserProfileActivity extends AppCompatActivity {

    private static final String ZIP = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);
        final EditText userProfileFirstName = (EditText) findViewById(R.id.user_profile_firstname);
        final EditText userProfileLastName = (EditText) findViewById(R.id.user_profile_lastname);
        final EditText userProfileZip = (EditText) findViewById(R.id.user_profile_zip);
        Button saveButton = (Button) findViewById(R.id.user_profile_saveBtn);

        saveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String firstName = userProfileFirstName.getText().toString();
                String lastName = userProfileLastName.getText().toString();
                String zip = userProfileZip.getText().toString();

                if(firstName.equalsIgnoreCase("") || lastName.equalsIgnoreCase("") || zip.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(), "Profile Incomplete", Toast.LENGTH_LONG).show();
                }

                if(!firstName.equalsIgnoreCase("") && !lastName.equalsIgnoreCase("") && !zip.equalsIgnoreCase("")){
                    Intent intent = new Intent(v.getContext(), RestaurantActivity.class);
                    intent.putExtra(ZIP, zip);
                    startActivity(intent);
                }
            }
        });
    }







}
