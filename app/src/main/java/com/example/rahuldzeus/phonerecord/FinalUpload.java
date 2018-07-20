package com.example.rahuldzeus.phonerecord;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class FinalUpload extends AppCompatActivity {
    Button buttonChoose,buttonUpload;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_upload);
        getSupportActionBar().hide();   //Hiding the toolbar
        backButton=findViewById(R.id.back_button);
        buttonUpload=findViewById(R.id.buttonUpload);
        buttonChoose=findViewById(R.id.buttonChoose);
        backButton.setOnClickListener(new View.OnClickListener() {      //if back button is pressed then the control moves to the previous activity
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoOrAudio=getIntent();
                String fileType=videoOrAudio.getStringExtra("TYPE");
                if (fileType.equals("video"))
                {
                    Intent videoFile=new Intent();/*Upload Video File*/
                }
                else if (fileType.equals("audio"))
                {
                    /*Upload Audio File*/
                }
            }
        });
    }
}
