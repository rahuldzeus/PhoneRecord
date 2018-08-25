package com.example.rahuldzeus.phonerecord;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.*;

public class ProfilePage extends AppCompatActivity {
    String message = "";
    TextView profileName,bio;
    RecyclerView recyclerView;
    List<FeedResponse> feedResponseList;
    FeedsAdapter adapter;
    CoordinatorLayout coordinatorLayout;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        getSupportActionBar().hide();
        profileName=findViewById(R.id.profile_name);
        bio=findViewById(R.id.bio);
        recyclerView=findViewById(R.id.recyclerView);
        coordinatorLayout=findViewById(R.id.coordinatorLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProfilePage.this));
        feedResponseList= new ArrayList<>();
        SharedPreferences username=getSharedPreferences("USERNAME",MODE_PRIVATE);
        profileName.setText(username.getString("username",null));
        SharedPreferences profileData=getSharedPreferences("PROFILE_DATA",MODE_PRIVATE);
        bio.setVisibility(View.VISIBLE);
        bio.setText(profileData.getString("bio", null));
        if (new ConnectionDetector().isConnectingToInternet(ProfilePage.this)){
                  /*Perform the fetching of the posts*/
            progressDialog=new ProgressDialog(ProfilePage.this);
            progressDialog.setTitle("Please Wait...");
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            JsonArrayRequest jsonArrayRequest=new JsonArrayRequest("https://storyclick.000webhostapp.com/your_post.php?username="+username.getString("username",null),new Response.Listener<JSONArray>(){

                @Override
                public void onResponse(JSONArray response) {

                    try {
                        feedResponseList.clear();
                        Gson gson=new Gson();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            /*Adding the values to List-For RecyclerView*/
                            FeedResponse feedResponse = gson.fromJson(String.valueOf(jsonObject), FeedResponse.class);
                            feedResponseList.add(feedResponse);
                        }
                        adapter = new FeedsAdapter(ProfilePage.this, feedResponseList);
                        recyclerView.setAdapter(adapter);
                        dialogClose();
                    }
                    catch (Exception e)
                    {
                        dialogClose();
                        Snackbar.make(coordinatorLayout,"Something went wrong",Snackbar.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialogClose();
                    Snackbar.make(coordinatorLayout,"Try again later",Snackbar.LENGTH_LONG).show();
                }
            }

            );
            RequestQueue requestQueue= Volley.newRequestQueue(ProfilePage.this);
            requestQueue.add(jsonArrayRequest);
        }
        else{
            Snackbar.make(findViewById(R.id.coordinatorLayout),"No Internet Connection",Snackbar.LENGTH_LONG).show();
        }
    }

    public void toEditProfilePage(View v) {
        Intent editProfile = new Intent(ProfilePage.this, EditProfile.class);
        startActivity(editProfile);
    }
    public void dialogClose(){
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
