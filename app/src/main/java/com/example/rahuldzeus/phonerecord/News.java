package com.example.rahuldzeus.phonerecord;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class News extends Fragment {

    Button music_button;
    View view;
    List<FeedResponse> feedResponseList;
    RecyclerView recyclerView;
    FeedsAdapter adapter;
    CoordinatorLayout coordinatorLayout;
    ProgressDialog progressDialog;
    String NEWS_URL="https://storyclick.000webhostapp.com/news_feeds.php";
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.news_layout,container,false);
        coordinatorLayout=view.findViewById(R.id.coordinatorLayout);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        /*Creating a new ArrayList to store the values*/
        feedResponseList= new ArrayList<>();
        //Checking Connection using user defined ConnectionDetector
        if (new ConnectionDetector().isConnectingToInternet(getActivity().getApplicationContext())) {
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Please Wait...");
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            /*Use Volley to request the data and set it in the list*/
            JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(NEWS_URL,new Response.Listener<JSONArray>(){

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
                        adapter = new FeedsAdapter(getActivity().getApplicationContext(), feedResponseList);
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
            RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
            requestQueue.add(jsonArrayRequest);
        }
        else{
            Snackbar.make(coordinatorLayout,"No Internet connection",Snackbar.LENGTH_LONG).show();
        }
        /*feedResponseList.add(new FeedResponse("rahul","Address", "Hello, this is your first message")); //Adding the data to be shown
        feedResponseList.add(new FeedResponse("Book","Android Development","Hello, this is your second message")); //Adding the data to be shown
        feedResponseList.add(new FeedResponse("rahul","Address", "Hello, this is your first message")); //Adding the data to be shown
        feedResponseList.add(new FeedResponse("Book","Android Development","Hello, this is your second message")); //Adding the data to be shown
        feedResponseList.add(new FeedResponse("rahul","Address", "Hello, this is your first message")); //Adding the data to be shown
        feedResponseList.add(new FeedResponse("Book","Android Development","Hello, this is your second message")); //Adding the data to be shown
*/
        return view;
    }
    public void dialogClose(){
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

}
