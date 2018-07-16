package com.example.rahuldzeus.phonerecord;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.*;

public class ProfilePage extends AppCompatActivity {
    TextView post;
    RequestQueue mRequestQueue;
    JSONObject jsonoBject = null;
    String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        post = findViewById(R.id.post);
        SharedPreferences sp = getSharedPreferences("PROFILE", MODE_PRIVATE);           //PROFILE SHARED PREFERENCE STORES PROFILE RELATED DATA
        final String postCount = sp.getString("post", null);
        if (postCount!=null)
        {
            post.setText(postCount);
        }
        else{
            post.setText("0");
        }


    }

    public void toEditProfilePage(View v) {
        Intent editProfile = new Intent(ProfilePage.this, EditProfile.class);
        startActivity(editProfile);
    }


}
