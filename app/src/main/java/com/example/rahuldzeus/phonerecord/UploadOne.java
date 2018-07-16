package com.example.rahuldzeus.phonerecord;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UploadOne extends AppCompatActivity {

    Button proceed_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_one);
        proceed_button=findViewById(R.id.proceed_button);
        proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toFinalUpload=new Intent(getApplicationContext(),FinalUpload.class);
                startActivity(toFinalUpload);
            }
        });
    }
}
