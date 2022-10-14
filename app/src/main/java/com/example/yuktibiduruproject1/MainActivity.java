package com.example.yuktibiduruproject1;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.Toast;

//The main class of the application
public class MainActivity extends AppCompatActivity {

    //Fields to be bound to GUI widgets
    Button mAddButton;                                  //Add Phone Number Button
    Button mCall;                                       //Call Button

    //Hidden variables not in the GUI
    String phoneNumber;                                 //Phone number entered in child activity
    Boolean isPhoneNumber, isCallButtonEnabled;         //Validity of Phone Number, Call Button enablement flag

    //Keys for 'saved state bundle'
    protected static final String phoneNumberKey = "PHONE_NUMBER_VALUE";
    protected static final String isPhoneNumberBoolKey = "PHONE_NUMBER_BOOL";
    protected static final String isCallButtonEnabledKey = "CALL_BUTTON_BOOL";

    //Called by the OS when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Parent class call
        super.onCreate(savedInstanceState);

        //Binding the layout file
        setContentView(R.layout.activity_main);

        //Bind the UI elements using their ids in the layout files
        mAddButton = findViewById(R.id.button);
        mCall = findViewById(R.id.button2);

        //Set initial values
        if(savedInstanceState == null)
        {
            phoneNumber = "";
            isPhoneNumber = false;
            isCallButtonEnabled = false;
        }
        //Set 'saved state' values
        else
        {
            phoneNumber = savedInstanceState.getString(phoneNumberKey);
            isPhoneNumber = savedInstanceState.getBoolean(isPhoneNumberBoolKey);
            isCallButtonEnabled = savedInstanceState.getBoolean(isCallButtonEnabledKey);
        }

        //Call button is enabled or disabled based on whether a mobile number ahs ben entered or not.
        //Initially set to false, enabled after navigating back from the first activity
        mCall.setEnabled(isCallButtonEnabled);

        //Listener for Add Phone Number Button
        mAddButton.setOnClickListener(
                v -> {
                    //Starts child activity expecting result
                    startActivityForResult((new Intent(MainActivity.this, PhoneNumberActivity.class)),0);
                }
        );

        //Listener for Call Button
        mCall.setOnClickListener(
                v -> {
                    //If Phone Number is valid then opens the dialer app, else displays invalidity toast message
                    if(isPhoneNumber)
                    {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        //Uncommenting below line will autofill entered phone number in the dialer
                        //intent.setData(Uri.parse("tel:" + phoneNumber));
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Phone Number entered is incorrect: " + phoneNumber, Toast.LENGTH_LONG).show();
                }
        );
    }

    //Save values required by the activity that will be otherwise lost in a configuration change, in the Saved Instance State
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(isPhoneNumberBoolKey,isPhoneNumber);
        outState.putBoolean(isCallButtonEnabledKey,isCallButtonEnabled);
        outState.putString(phoneNumberKey,phoneNumber);
    }

    //This method is called when the child activity sets a result to be sent to parent activity (this)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Parent class call
        super.onActivityResult(requestCode, resultCode, data);

        //Enabling call button after navigating back from the child activity
        isCallButtonEnabled = true;
        mCall.setEnabled(true);

        //Extracting phone number from the intent extras
        if(data != null && data.getExtras() != null)
        phoneNumber = data.getExtras().getString(phoneNumberKey);

        //Checking result code and setting phone number validity flag
        isPhoneNumber = (resultCode == RESULT_OK);

    }
}