package com.example.rahuldzeus.phonerecord;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;


import java.util.Arrays;

public class WelcomePage extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    Button goHomePageFromLogin;
    ConnectionDetector connectionDetector;
    CoordinatorLayout coordinatorLayout;

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
                if (connectionDetector.isConnectingToInternet(WelcomePage.this)) {

                    Intent goingToHomePageFromLogin = new Intent(getApplicationContext(), HomePage.class);
                    startActivity(goingToHomePageFromLogin);
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
