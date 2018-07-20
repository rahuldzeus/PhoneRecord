package com.example.rahuldzeus.phonerecord;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    public TextView mTextMessage;
    short backCount=0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    News news=new News();
                    ft.addToBackStack("news");
                    ft.replace(R.id.homepage_frame,news);
                    ft.commit();//Fragment

                    return true;
                case R.id.navigation_search:
                    Search searchInstance=new Search();
                    ft.addToBackStack("searchInstance");
                    ft.replace(R.id.homepage_frame,searchInstance);
                    ft.commit();

                    //Fragment
                  return true;
                case R.id.navigation_upload:
                    Intent toUploadOne=new Intent(getApplicationContext(),UploadOne.class);
                    startActivity(toUploadOne);
                    //Fragment
                    return true;
                case R.id.navigation_notification:
                    Notify notification=new Notify();
                    ft.addToBackStack("notification");
                    ft.replace(R.id.homepage_frame,notification);
                    ft.commit();
                    //Fragment
                    return true;
                case R.id.navigation_profile:
                    Intent toProfile=new Intent(HomePage.this,ProfilePage.class); //Activity
                    startActivity(toProfile);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);




        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        News news=new News();
        ft.addToBackStack("news");
        ft.replace(R.id.homepage_frame,news);
        ft.commit();//Fragment

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
   return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.rate_us:
    //Will Opem the googlePlay and rating will be taken
                return true;
            case R.id.go_premium:
                Intent toGoPremium=new Intent(HomePage.this,GoPremium.class);
                startActivity(toGoPremium);
                return true;
           /* case R.id.faq:
               Intent toFAQ=new Intent(HomePage.this,Faq.class);
               startActivity(toFAQ);
                break;
            case R.id.invite:
               Intent toInvite=new Intent(HomePage.this,Invite.class);
               startActivity(toInvite);
                return true;
            case R.id.terms:
               Intent toTerms=new Intent(HomePage.this,Terms.class);
               startActivity(toTerms);
                return true;
            case R.id.privacy:
                Intent toPrivacy=new Intent(HomePage.this,Privacy.class);
                startActivity(toPrivacy);

                return true;*/
            case R.id.log_out:      //Session will be removed and the user will be sent to the welcome page
                SharedPreferences logout=getSharedPreferences("USERNAME",MODE_PRIVATE);
                SharedPreferences.Editor loggingOut=logout.edit();
                loggingOut.putString("USERNAME",null);
                loggingOut.commit();
                Intent toLoginAfterLogout=new Intent(HomePage.this,WelcomePage.class);
                startActivity(toLoginAfterLogout);
                Toast.makeText(HomePage.this, "HELLO Logout", Toast.LENGTH_SHORT).show();
                return true;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
