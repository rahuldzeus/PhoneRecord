package com.example.rahuldzeus.phonerecord;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationPage extends AppCompatActivity {

    Button signUp;
    EditText userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        try {
            getSupportActionBar().hide();
        }
        catch(NullPointerException e)
        {
            Toast.makeText(RegistrationPage.this,"Enjoy",Toast.LENGTH_SHORT).show();
        }
        signUp = findViewById(R.id.signUpButton);
        userPhone = findViewById(R.id.userPhoneNumber);
        String userPhoneNumber=userPhone.getText().toString();  //Phone Number is in String, Convert it to integer before using further
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent goingToPhoneConfirmation = new Intent(RegistrationPage.this, PhoneConfirmation.class);
                        startActivity(goingToPhoneConfirmation);
                }
            });

    }
}
