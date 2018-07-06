package com.example.rahuldzeus.phonerecord;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ProfilePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
    }
    public void toEditProfilePage(View v)
    {
        Intent editProfile=new Intent(ProfilePage.this,EditProfile.class);
        startActivity(editProfile);
    }
}
