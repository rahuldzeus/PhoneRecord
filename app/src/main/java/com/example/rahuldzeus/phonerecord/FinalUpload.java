package com.example.rahuldzeus.phonerecord;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class FinalUpload extends AppCompatActivity {
    ImageButton backButton;
    Button uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_upload);
        getSupportActionBar().hide();   //Hiding the toolbar
        backButton=findViewById(R.id.back_button);
        uploadButton=findViewById(R.id.upload_button);
        backButton.setOnClickListener(new View.OnClickListener() {      //if back button is pressed then the control moves to the previous activity
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectFile=new Intent(Intent.ACTION_GET_CONTENT);
                selectFile.setType("file/*");
                Intent chooser=Intent.createChooser(selectFile,"Select File");
                 if (chooser!=null)
                 {
                     Toast.makeText(FinalUpload.this,"CAME HERE", Toast.LENGTH_LONG).show();
                 }
            }
        });
    }
}
