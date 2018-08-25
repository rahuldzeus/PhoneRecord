package com.example.rahuldzeus.phonerecord;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.Header;

public class FinalUploadVideo extends AppCompatActivity {
    Button buttonChoose, buttonUpload;
    ImageButton backButton;
    int VIDEO_REQUEST = 1;
    String username, fileName, filePath;
    FinalUploadAudio upload;
    ProgressDialog dialog;
    ImageView rocket;
    String SERVER_URL="https://storyclick.000webhostapp.com/upload_file.php";
    File video;
    String messageToSend;
    EditText message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_upload);
        rocket=findViewById(R.id.rocket_image);
        rocket.setImageResource(R.drawable.uploadimage);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getSupportActionBar().hide();   //Hiding the toolbar
        backButton = findViewById(R.id.back_button);
        buttonUpload = findViewById(R.id.buttonUpload);
        message=findViewById(R.id.text_message);
        buttonChoose = findViewById(R.id.buttonChoose);
        SharedPreferences sp = getSharedPreferences("USERNAME", MODE_PRIVATE);
        username = sp.getString("username", null);
        backButton.setOnClickListener(new View.OnClickListener() {      //if back button is pressed then the control moves to the previous activity
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rocket.setImageResource(R.drawable.uploadimage);
                Intent video = new Intent();
                video.setType("video/*");
                video.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(video, "Select the video"), VIDEO_REQUEST);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == VIDEO_REQUEST) {
                buttonUpload.setVisibility(View.VISIBLE);/*set Visibility for buttonUpload*/
                message.setVisibility(View.VISIBLE);/*set Visibility for message*/
                Uri videoUri = data.getData();
                if (videoUri.getScheme().toString().compareTo("content") == 0) {
                    Cursor cursor = getContentResolver().query(videoUri, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        Uri filePathUri = Uri.parse(cursor.getString(column_index));
                        fileName = filePathUri.getLastPathSegment().toString();
                        filePath = filePathUri.getPath();
                        video = new File(filePath);
                        upload = new FinalUploadAudio();
                        buttonUpload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog=new ProgressDialog(FinalUploadVideo.this);
                                dialog.setMessage("Please wait");
                                dialog.setTitle("Uploading...");
                                dialog.setCancelable(false);
                                dialog.show();
                                messageToSend=message.getText().toString(); /*Message body fetched*/
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        /*Uploading begin*/
                                        uploadFile(filePath);
                                        /*Uploading End*/
                                        if (dialog.isShowing())
                                        {
                                            dialog.dismiss();
                                        }
                                    }
                                }).start();
                            }
                        });

                    }
                }
            }

        }
    }
    public void uploadFile(final String selectedFilePath){

        int serverResponseCode = 0;


        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FinalUploadVideo.this,"File Error",Toast.LENGTH_LONG).show();
                }
            });
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);
                //Write form fields


                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());


                //writing bytes to data outputstream
                /*SENDING FORM DATA-START*/
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"message"+ lineEnd);
                dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(messageToSend+ lineEnd);
                dataOutputStream.flush();
                /*SENDING FORM DATA-END*/


                /*SENDING FORM DATA-START*/
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"username"+ lineEnd);
                dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(username+ lineEnd);
                dataOutputStream.flush();
                /*SENDING FORM DATA-END*/

                /*SENDING FILE DATA-START*/
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ selectedFilePath + "\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                /*SENDING FILE DATA-END*/



                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();
                if (serverResponseMessage.equals("OK")) {
                    //response code of 200 indicates the server status OK
                    if (serverResponseCode == 200) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                buttonUpload.setVisibility(View.GONE);
                                rocket.setImageResource(R.drawable.checked);
                                message.setVisibility(View.GONE);
                                message.setText("");
                            }
                        });
                    }
                    else{
                        rocket.setImageResource(R.drawable.error);
                    }
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();


                /*Response from Server start*/
                InputStream is = connection.getInputStream();
                byte[] b1 = new byte[1024];
                final StringBuffer resp = new StringBuffer();
                while (is.read(b1) != -1)
                    resp.append(new String(b1));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FinalUploadVideo.this,resp.toString(),Toast.LENGTH_LONG).show();
                        if (resp.toString().substring(0,13).equalsIgnoreCase("File Uploaded"))
                        {
                            rocket.setImageResource(R.drawable.checked);
                        }
                        else{
                            rocket.setImageResource(R.drawable.error);
                        }
                    }
                });

                connection.disconnect();

                /*Response from server end*/
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FinalUploadVideo.this,"File Not Found",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(FinalUploadVideo.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(FinalUploadVideo.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
        }

    }


}