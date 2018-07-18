package com.example.rahuldzeus.phonerecord;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toolbar;

import java.util.Objects;

public class AudioPlayingPlayer extends AppCompatActivity {
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_playing_player);
        backButton=findViewById(R.id.back_button);
        try {
            getSupportActionBar().hide();
        }catch(NullPointerException e) {
            e.printStackTrace();
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioPlayingPlayer.super.onBackPressed();       //on pressing back button the control will go back to news feeds Fragment
            }
        });
    }
}
