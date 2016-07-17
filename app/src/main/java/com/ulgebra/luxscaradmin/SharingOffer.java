package com.ulgebra.luxscaradmin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class SharingOffer extends AppCompatActivity {

    AsyncTask<Void, Void, Void> mTask;
    String jsonString,oldCusCha,newCusCha;
    String url = "http://luxscar.com/luxscar_app";
    Button b;
    ProgressDialog dialog;
    String url_select;
    TextView txt1,txt2,txt3;
    AutoCompleteTextView newCusTxtA,oldCusTxtA;
    int i,j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing_offer);

         txt1 = (TextView) findViewById(R.id.oldCusto);

        txt2 = (TextView) findViewById(R.id.newCusto);

                txt3=(TextView)findViewById(R.id.longTxt);
                newCusTxtA=(AutoCompleteTextView)findViewById(R.id.newCust);
        oldCusTxtA=(AutoCompleteTextView)findViewById(R.id.oldCust);
        Button btn=(Button)findViewById(R.id.upload_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                oldCusCha = oldCusTxtA.getText().toString();
                 newCusCha = newCusTxtA.getText().toString();

                //check whether the msg empty or not


                new MyAsyncTaskq().execute();

            }
        });

         url_select = "http://luxscar.com/luxscar_app/sharingOfferDets.php";

        new MyAsyncTask().execute(url_select);


    }

    class MyAsyncTask extends AsyncTask<String, String, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(SharingOffer.this);
        InputStream inputStream = null;
        String result = "";

        @Override
        protected Void doInBackground(String... params) {




            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                // Set up HTTP post

                // HttpClient is more then less deprecated. Need to change to URLConnection
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(url_select);
                httpPost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                // Read content & Log
                inputStream = httpEntity.getContent();
            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsuppongException", e1.toString());
                e1.printStackTrace();
            } catch (ClientProtocolException e2) {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }
            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();

                Log.v("fetched result",result);


            } catch (Exception e) {
                Log.e("StrgBudg & BufredRdr", "Error converting result " + e.toString());
            }
            return null;
        }


        protected void onPreExecute() {

            progressDialog.setMessage("Please wait ...");
            progressDialog.show();

        }
        protected void onPostExecute(Void v) {


            //parse JSON data
            try {
                JSONArray jArray = new JSONArray(result);
                for(i=0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);

                    String oldCutAmt = jObject.getString("old_customer");
                    String newCusAmt = jObject.getString("new_customer");

                    txt1.setText("Currently Rs."+oldCutAmt);
                    txt2.setText("Currently Rs."+newCusAmt);

                    txt3.setText("When a new user puts referrer number , New Customer gets Rs."+newCusAmt+" as discount balance in account and Referrer gets Rs."+oldCutAmt+"\n");


                }
                this.progressDialog.dismiss();
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            }
        }
    }
    class MyAsyncTaskq extends AsyncTask<String, String, Void>{
        ProgressDialog progressDialog2=new ProgressDialog(SharingOffer.this);

        @Override
        protected Void doInBackground(String... params) {



            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://luxscar.com/luxscar_app/updateSharingOffer.php");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("newCuss", newCusCha));
                nameValuePairs.add(new BasicNameValuePair("oldCuss", oldCusCha));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpclient.execute(httppost);
//                Toast.makeText(getApplicationContext(),"Sent",Toast.LENGTH_SHORT).show();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPreExecute() {
            progressDialog2.setMessage("Updating...");
            progressDialog2.show();

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            progressDialog2.dismiss();
            Toast.makeText(getApplicationContext(),"Successfully Updated",Toast.LENGTH_LONG);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            super.onPostExecute(aVoid);

        }


    }

}
