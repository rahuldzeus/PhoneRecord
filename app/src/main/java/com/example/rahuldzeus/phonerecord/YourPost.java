package com.example.rahuldzeus.phonerecord;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YourPost extends Fragment {
    View view;
    String NEWS_URL="https://storyclick.000webhostapp.com/your_post.php";
    RecyclerView recyclerView;
    List<FeedResponse> feedResponseList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.news_layout,container,false);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        /*Creating a new ArrayList to store the values*/
        feedResponseList= new ArrayList<>();
        /*Use Volley and add the data in ArrayList*/
        ConnectionDetector connectionDetector = new ConnectionDetector();       //Checking Connection using user defined ConnectionDetector
        if (connectionDetector.isConnectingToInternet(getActivity().getApplicationContext())) {
            StringRequest stringRequest=new StringRequest(Request.Method.POST,NEWS_URL,new Response.Listener<String>(){

                @Override
                public void onResponse(String response) {

                    try {
                        JSONArray jsonArray=new JSONArray(response);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            try{
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                /*Sending the values to Adapter-For RecyclerView*/
                                feedResponseList.add(new FeedResponse(jsonObject.getString("username"),jsonObject.getString("fileAddress"), jsonObject.getString("message")));
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(getActivity().getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }

            ){
                protected Map<String,String> getParams()throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();
                    params.put("NEWS_FEEDS","SEND_FEEDS");
                    return params;
                }
            };
            RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
            requestQueue.add(stringRequest);
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(),"No Internet Connection", Toast.LENGTH_LONG).show();
        }
       /* feedResponseList.add(new FeedResponse("rahul","Address", "Hello, this is your first message")); //Adding the data to be shown
        feedResponseList.add(new FeedResponse("Book","Android Development","Hello, this is your second message")); //Adding the data to be shown
        feedResponseList.add(new FeedResponse("rahul","Address", "Hello, this is your first message")); //Adding the data to be shown
        feedResponseList.add(new FeedResponse("Book","Android Development","Hello, this is your second message")); //Adding the data to be shown
        feedResponseList.add(new FeedResponse("rahul","Address", "Hello, this is your first message")); //Adding the data to be shown
        feedResponseList.add(new FeedResponse("Book","Android Development","Hello, this is your second message")); //Adding the data to be shown
*/
        FeedsAdapter adapter=new FeedsAdapter(getActivity().getApplicationContext(),feedResponseList);
        recyclerView.setAdapter(adapter);
        return view;
    }
}

