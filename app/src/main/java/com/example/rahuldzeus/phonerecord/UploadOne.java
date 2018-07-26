package com.example.rahuldzeus.phonerecord;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class UploadOne extends AppCompatActivity {

    ImageButton videoUpload, audioUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_one);
        getSupportActionBar().hide();
        ImageButton back_button=findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        videoUpload=findViewById(R.id.to_video_upload_activity);
        audioUpload=findViewById(R.id.to_audio_upload_activity);
        videoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toVideoUploadActivity=new Intent(UploadOne.this,FinalUploadVideo.class);
                startActivity(toVideoUploadActivity);
            }
        });
        audioUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAudioUploadActivity=new Intent(UploadOne.this,FinalUploadAudio.class);
                startActivity(toAudioUploadActivity);
            }
        });

    }
}
