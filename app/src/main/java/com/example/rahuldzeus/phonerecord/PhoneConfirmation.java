package com.example.rahuldzeus.phonerecord;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PhoneConfirmation extends AppCompatActivity {

    Button nextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_confirmation);
        nextButton=findViewById(R.id.nextButton);
        try {
            getSupportActionBar().hide();
        }
        catch(NullPointerException e)
        {
            Toast.makeText(PhoneConfirmation.this,"Enjoy",Toast.LENGTH_SHORT).show();
        }
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goingToNextRegistrationPage=new Intent(PhoneConfirmation.this,RegistrationpageTwo.class);
                startActivity(goingToNextRegistrationPage);
            }
        });

    }
}
