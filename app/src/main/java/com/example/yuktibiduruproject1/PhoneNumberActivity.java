package com.example.yuktibiduruproject1;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//The child class of the application: PhoneNumberActivity
public class PhoneNumberActivity extends AppCompatActivity {

    //Fields to be bound to GUI widgets
    Button mDone;                                       //The 'Done' button
    TextView mEditText;                                 //The edit text field

    //Hidden variables not in the GUI
    Boolean isPhoneNumber;                              //Validity flag of the Phone Number entered
    String PhoneNumber;                                 //Phone Number entered
    Intent intent = new Intent();                       // New intent initialized for use in the activity

    // The key in the 'saved state' bundle
    protected static final String phoneNumberKey = "PHONE_NUMBER_VALUE";

    //Called by the OS when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Parent class call
        super.onCreate(savedInstanceState);

        //Binding the layout file
        setContentView(R.layout.activity_phone_number);

        //Bind the UI elements using their ids in the layout files
        mDone = findViewById(R.id.button3);
        mEditText = findViewById(R.id.textView3);

        //Set initial values
        if(savedInstanceState == null)
        {
            mEditText.setText(null);
            PhoneNumber = "";
        }
        //Set 'saved state' values
        else
        {
            PhoneNumber = savedInstanceState.getString(phoneNumberKey);
            mEditText.setText(PhoneNumber);
        }

        //Listener for Done Button
        mDone.setOnClickListener(
                v -> setResultForActivity()
        );

        //Listener for Edit Text Field
        mEditText.setOnEditorActionListener( (v, actionId, event) ->
        {
            setResultForActivity();
            return true;
        });
    }

    //Save values required by the activity that will be otherwise lost in a configuration change, in the Saved Instance State
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString(phoneNumberKey, PhoneNumber);
    }

    //Function to validate the phone number entered using Regex Expressions and set the result for the parent activity
    public void setResultForActivity()
    {
        String PhoneNumber = mEditText.getText().toString();

        //Regex Pattern for (312) 555-1212 or (312)555-1212
        Pattern pattern = Pattern.compile("^[(][1-9]\\d{2}[)]\\s?\\d{3}[-]\\d{4}$");
        //Regex Pattern for 3125551212
        Pattern pattern2 = Pattern.compile("^[1-9]\\d{9}$");

        Matcher matcher = pattern.matcher(PhoneNumber);
        Matcher matcher2 = pattern2.matcher(PhoneNumber);

        //Validating the phone number using the pattern
        isPhoneNumber = (matcher2.matches()) || (matcher.matches());

        //Saving the phone number as an extra in the intent to be sent back to the parent activity
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            intent.putExtras(extras);
        }
        intent.putExtra(phoneNumberKey,PhoneNumber);

        //Setting result for the parent activity based on validity of the phone number
        if(isPhoneNumber)
        {
            setResult(RESULT_OK,intent);
        }
        else
            setResult(RESULT_CANCELED, intent);
        //Finishing the activity
        finish();
    }
}