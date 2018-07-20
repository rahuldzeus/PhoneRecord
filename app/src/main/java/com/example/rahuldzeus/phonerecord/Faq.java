package com.example.rahuldzeus.phonerecord;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.media.CamcorderProfile.get;

public class Faq extends AppCompatActivity {

    String url=null;
    String data="";
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        listView=findViewById(R.id.list_view);
        /*Sending request to the Server*/
        StringRequest faq=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               /* List list=new ArrayList();
                try {
                    JSONArray ja=new JSONArray(response);
                    for (int i=0; i<response.length(); i++)
                    {
                        data=ja.getString(i);
                        list.add(data);
                    }
                    ArrayAdapter faqQuestion=new ArrayAdapter(Faq.this,R.layout.faq_list,list);
                    listView.setAdapter(faqQuestion);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
*/


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("faq","faq");
                return params;
            }
        };
        RequestQueue faqRequest= Volley.newRequestQueue(Faq.this);
        faqRequest.add(faq);
    }
}
