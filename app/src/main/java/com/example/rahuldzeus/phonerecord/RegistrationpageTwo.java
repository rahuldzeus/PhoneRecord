package com.example.rahuldzeus.phonerecord;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.provider.CalendarContract.CalendarCache.URI;

public class RegistrationpageTwo extends AppCompatActivity {
    Button toProfile;
    String name,password,cpassword;
    EditText nameUI,passwordUI,cpasswordUI;
    TextView invalid_password,invalid_cpassword,invalid_username;
    RequestQueue mRequestQueue;
    String REGISTER_URL="https://storyclick.000webhostapp.com/registration.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrationpage_two);
        try {
            getSupportActionBar().hide();
        }
        catch(NullPointerException e)
        {
            Toast.makeText(RegistrationpageTwo.this,"Enjoy",Toast.LENGTH_SHORT).show();
        }
        toProfile=findViewById(R.id.finish_button);
        toProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameUI = findViewById(R.id.username);
                passwordUI = findViewById(R.id.password);
                cpasswordUI = findViewById(R.id.c_password);
                invalid_cpassword=findViewById(R.id.invalid_cpassword);
                invalid_password=findViewById(R.id.invalid_password);
                invalid_username=findViewById(R.id.invalid_username);
                name = nameUI.getText().toString().trim();
                password = passwordUI.getText().toString().trim();
                cpassword = cpasswordUI.getText().toString().trim();
                invalid_password.setVisibility(View.GONE);                  //After the response
                invalid_cpassword.setVisibility(View.GONE);
                invalid_username.setVisibility(View.GONE);
                if (name.isEmpty()) {
                    invalid_username.setText(("Username is required"));
                    invalid_username.setVisibility(View.VISIBLE);

                } else {
                    invalid_username.setVisibility(View.GONE);
                    if (password.equals(cpassword)) {
                        //Server Sending Code start

                        StringRequest stringRequest=new StringRequest(Request.Method.POST, REGISTER_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("SUCCESS"))
                                {
                                    Intent toWelcomePage=new Intent(RegistrationpageTwo.this,WelcomePage.class);
                                    startActivity(toWelcomePage);
                                }
                                else if (response.equals("CONNECTION_ERROR")){
                                    Toast.makeText(RegistrationpageTwo.this,"Try again later",Toast.LENGTH_LONG).show();

                                }
                                else if (response.equals("Username already exist")){
                                    TextView errorAfterResponse=findViewById(R.id.error_after_response);
                                    errorAfterResponse.setText(("Username already exist"));
                                    errorAfterResponse.setVisibility(View.VISIBLE);
                                }
                                else if(response.equals("ERROR")){
                                    Toast.makeText(RegistrationpageTwo.this,"Unknow Error",Toast.LENGTH_LONG).show();

                                }
                                else if(response.equals("REQUEST_ERROR")){
                                    Toast.makeText(RegistrationpageTwo.this,"Error",Toast.LENGTH_LONG).show();

                                }
                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(RegistrationpageTwo.this, "Try again later", Toast.LENGTH_LONG).show();
                                    }
                                }
                        ){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> params=new HashMap<String,String>();
                                params.put("username",name);
                                params.put("password",password);
                                params.put("phone","7452");
                                return params;
                            }
                        };
                        RequestQueue requestQueue=Volley.newRequestQueue(RegistrationpageTwo.this);
                        requestQueue.add(stringRequest);


                        //Server Sending Code end



                    } else {
                        invalid_password.setText(("Password not match"));
                        invalid_cpassword.setText(("Password not match"));
                        invalid_password.setVisibility(View.VISIBLE);
                        invalid_cpassword.setVisibility(View.VISIBLE);

                    }


                }
            }
        });

    }
}
