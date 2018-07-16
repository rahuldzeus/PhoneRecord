package com.example.rahuldzeus.phonerecord;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.rahuldzeus.phonerecord.R.style.AppTheme;

public class EditProfile extends AppCompatActivity {

    String gender[]={"Male","Female","Not Specified"};
    Spinner spinner;
    ImageButton save;
    RequestQueue mRequestQueue;
    String user_name;
    String url="https://storyclick.000webhostapp.com/edit_profile.php";
    EditText onlineName,onlineUsername,onlineBio,onlineEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        onlineName=findViewById(R.id.online_name);
        onlineUsername=findViewById(R.id.online_username);
        onlineBio=findViewById(R.id.online_bio);
        onlineEmail=findViewById(R.id.online_email);
        SharedPreferences sharedPreferences=getSharedPreferences("USERNAME",MODE_PRIVATE);
        user_name=sharedPreferences.getString("username",null);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo=new JSONObject(response);
                    String message=jo.getString("message");
                    switch (message) {
                        case "INAPPROPRIATE_REQUEST":
                            Toast.makeText(getApplicationContext(), "Try again later", Toast.LENGTH_SHORT).show();
                            break;
                        case "SUCCESS":
//                            onlineName.setText(jo.getString("name"));
                            onlineBio.setText(jo.getString("bio"));
                            onlineUsername.setText(jo.getString("username"));
//                            onlineEmail.setText(jo.getString("email"));

                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProfile.this, "Try again Later", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_name", user_name);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        requestQueue.add(stringRequest);


        save=findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {        //Fetch and save the data into the database
            @Override
            public void onClick(View v) {
                String saveUrl="https://storyclick.000webhostapp.com/save_profile.php";
               final String sendingName=onlineName.getText().toString().trim();
               final String sendingBio=onlineBio.getText().toString().trim();
               final String sendingUsername=onlineUsername.getText().toString().trim();
               final String sendingEmail=onlineEmail.getText().toString().trim();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, saveUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jo=new JSONObject(response);
                            String message=jo.getString("message");
                            switch (message) {          //RESPONSE FROM SERVER
                                case "DATABASE_CONNECTION_ERROR":
                                    Toast.makeText(EditProfile.this,"Connection Error",Toast.LENGTH_LONG).show();
                                   break;
                                case "SUCCESS":
                                    onlineEmail.setText(sendingEmail);
                                    onlineUsername.setText(sendingUsername);
                                    onlineBio.setText(sendingBio);
                                    onlineName.setText(sendingName);
                                    Toast.makeText(EditProfile.this,"Profile Updated",Toast.LENGTH_LONG).show();

                                    break;
                                case "REQUEST_ERROR":
                                    Toast.makeText(EditProfile.this,"Try again later",Toast.LENGTH_LONG).show();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(EditProfile.this, "Try again Later", Toast.LENGTH_LONG).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("catchingName", sendingName);
                        params.put("catchingBio", sendingBio);
                        params.put("catchingUsername", sendingUsername);
                        params.put("catchingEmail", sendingEmail);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
                requestQueue.add(stringRequest);

            }
        });

    }


}

