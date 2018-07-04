package com.example.rahuldzeus.phonerecord;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RegistrationpageTwo extends AppCompatActivity {
    Button toProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrationpage_two);
        try {
            getSupportActionBar().hide();
        }
        catch(NullPointerException e)
        {
            Toast.makeText(RegistrationpageTwo.this,"Enjoy",Toast.LENGTH_SHORT).show();
        }
    }
}
