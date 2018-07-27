package com.example.rahuldzeus.phonerecord;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class FinalUploadAudio extends AppCompatActivity {
    Button buttonChoose, buttonUpload;
    ImageButton backButton;
    private String SERVER_URL = "https://storyclick.000webhostapp.com/upload_file.php";
    int AUDIO_REQUEST = 1;
    ProgressDialog dialog;
    String fileName, username, filePath;
    File audio;
    ImageView rocket;
    Uri audioUri=null;

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
        buttonChoose = findViewById(R.id.buttonChoose);
        buttonUpload.setVisibility(View.GONE);

        SharedPreferences sp = getSharedPreferences("USERNAME", MODE_PRIVATE);
        username = sp.getString("username", null);     /*Fetch username from the Shared Preference and send to the Server*/
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
                /*Selecting the audio using Intent*/
                Intent audio = new Intent();
                audio.setType("audio/*");
                audio.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(audio, "Select the file"), AUDIO_REQUEST);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == AUDIO_REQUEST) {
                buttonUpload.setVisibility(View.VISIBLE);/*set Visibility for buttonUpload*/
               audioUri = data.getData();  //URI
                if (audioUri.getScheme().toString().compareTo("content") == 0) {
                    Cursor cursor = getContentResolver().query(audioUri, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        final Uri filePathUri = Uri.parse(cursor.getString(column_index));
                        fileName = filePathUri.getLastPathSegment().toString();
                        filePath = filePathUri.getPath();
                        audio = new File(filePath);
                        buttonUpload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog=new ProgressDialog(FinalUploadAudio.this);
                                dialog.setMessage("Please wait");
                                dialog.setTitle("Uploading...");
                                dialog.setCancelable(false);
                                dialog.show();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        /*Uploading begin*/

                                        uploadFile(filePath);
                                       if (dialog.isShowing())
                                       {
                                           dialog.dismiss();
                                       }

                                        /*Uploading End*/
                                    }
                                }).start();

                            }

                        });

                    }
                }
            }
        }
    }
    /*Start Uploading*/
    public void uploadFile(final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FinalUploadAudio.this,"File Error",Toast.LENGTH_LONG).show();
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


                            }
                        });
                    }
                    else
                    {
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
                        Toast.makeText(FinalUploadAudio.this,resp.toString(),Toast.LENGTH_LONG).show();
                    }
                });


                connection.disconnect();

                /*Response from server end*/
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FinalUploadAudio.this,"File not found",Toast.LENGTH_LONG).show();

                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(FinalUploadAudio.this,"Connection Error",Toast.LENGTH_LONG).show();


            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(FinalUploadAudio.this,"File error",Toast.LENGTH_LONG).show();

            }
        }
    }
}