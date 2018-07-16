package com.example.rahuldzeus.phonerecord;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class News extends Fragment {
    Button music_button;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.news_layout,container,false);
        music_button=view.findViewById(R.id.music_button);
        music_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),AudioPlayingPlayer.class);
                startActivity(intent);

            }
        });
        //  Listener Methods and Business Logic will be here
        return view;
    }



}
