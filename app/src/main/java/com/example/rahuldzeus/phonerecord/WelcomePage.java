package com.example.rahuldzeus.phonerecord;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WelcomePage extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    Button goHomePageFromLogin;
    ConnectionDetector connectionDetector;
    CoordinatorLayout coordinatorLayout;
    EditText login_username, login_password;
    String LOGIN_URL="https://storyclick.000webhostapp.com/login.php";
    String usernameToSend, passwordToSend;
    TextView error_response;

    //Signin button
    private SignInButton signInButton;
    private static final String EMAIL = "email";

    //Signing Options
    private GoogleSignInOptions gso;

    //google api client
    private GoogleApiClient mGoogleApiClient;

    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {        //OnCreate Method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        error_response=findViewById(R.id.error_response);

        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            Toast.makeText(WelcomePage.this, "Enjoy the Application", Toast.LENGTH_SHORT).show();
        }
        connectionDetector = new ConnectionDetector();
        goHomePageFromLogin = findViewById(R.id.goHomePageFromLogin);
        goHomePageFromLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_response.setVisibility(View.GONE);
                if (connectionDetector.isConnectingToInternet(WelcomePage.this)) {
                    //Start Fetching username and password from the page

                    login_username = findViewById(R.id.username_from_login);
                    login_password = findViewById(R.id.password_from_login);
                    usernameToSend = login_username.getText().toString();
                    passwordToSend = login_password.getText().toString();
                    if (usernameToSend.isEmpty() || passwordToSend.isEmpty()) {
                        error_response.setText(("Enter Username and Password"));
                        error_response.setVisibility(View.VISIBLE);

                    } else {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                switch (response) {
                                    case "LOGIN GRANTED":
                                        SharedPreferences sp=getSharedPreferences("USERNAME",MODE_PRIVATE);        //Shared Preferences contain USERNAME to track the user
                                        SharedPreferences.Editor editor=sp.edit();
                                        editor.putString("username",usernameToSend);
                                        editor.commit();
                                        Intent toHomePage = new Intent(WelcomePage.this, HomePage.class);
                                        startActivity(toHomePage);
                                        break;
                                    case "ERROR IN CONNECTION":
                                        Toast.makeText(WelcomePage.this, "Try again later", Toast.LENGTH_LONG).show();

                                        break;
                                    case "LOGIN DETAILS NOT FOUND":
                                        error_response.setText(("Wrong username or password"));
                                        error_response.setVisibility(View.VISIBLE);

                                        break;
                                }
                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(WelcomePage.this, "Try again Later", Toast.LENGTH_LONG).show();
                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("username", usernameToSend);
                                params.put("password", passwordToSend);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(WelcomePage.this);
                        requestQueue.add(stringRequest);


                        //End Fetching username and password from the page

                    }
                }
                else
                {
                    Toast.makeText(WelcomePage.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

            //Initializing google signin option
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            //Initializing signinbutton
            signInButton = (SignInButton) findViewById(R.id.sign_in_button);
            signInButton.setSize(SignInButton.SIZE_WIDE);
            signInButton.setScopes(gso.getScopeArray());

            //Initializing google api client
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();


            //Setting onclick listener to signing button
            signInButton.setOnClickListener(this);
        }


    //This function will option signing intent
    private void signIn() {
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }
    }



    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
           //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();


                                                                                    //Store the Name and Email in DATABASE
            //textViewName.setText(acct.getDisplayName());
            //textViewEmail.setText(acct.getEmail());
/*
            //Initializing image loader
            imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
                    .getImageLoader();

            imageLoader.get(acct.getPhotoUrl().toString(),
                    ImageLoader.getImageListener(profilePhoto,
                            R.mipmap.ic_launcher,
                            R.mipmap.ic_launcher));

            //Loading image
            profilePhoto.setImageUrl(acct.getPhotoUrl().toString(), imageLoader);
            */
Intent goingToNewsFeed=new Intent(WelcomePage.this,HomePage.class);
startActivity(goingToNewsFeed);
        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onClick(View v) {
        if (v == signInButton) {
            //Calling signin
            if (connectionDetector.isConnectingToInternet(WelcomePage.this)) {
                signIn();
            }
            else{
                Toast.makeText(WelcomePage.this, "No internet connection", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(WelcomePage.this,"Problem occured",Toast.LENGTH_LONG).show();

    }



    public void toRegistrationPage(View view)
    {
        if (connectionDetector.isConnectingToInternet(WelcomePage.this)) {
            Intent goingToRegistrationPage = new Intent(WelcomePage.this, RegistrationPage.class);
            startActivity(goingToRegistrationPage);
        }
        else{
            Toast.makeText(WelcomePage.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

}
