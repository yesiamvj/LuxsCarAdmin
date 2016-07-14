package com.ulgebra.luxscaradmin;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddOffer extends AppCompatActivity {

    String couponCode,minPurAmt,maxDisc,discPerc,discPrice,expDatee;
    AutoCompleteTextView couponCodeTxt,minPurAmtTxt,maxDiscTxt,discPercTxt,discPriceTxt;
    DatePicker expDateeTxt;


    int one_img=0;
    int one_img_sts=-1;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect;
    private ImageView ivImage;
    private String userChoosenTask;
    int PICK_IMAGE_MULTIPLE = 1;

    int tot_en=-1;
    Button upload_btn_inp;
    String[] tot_files=new String[30];
    ProgressDialog dialog = null;
    HttpEntity resEntity;
    List<String> imagesEncodedList;
    int serverResponseCode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        try{

            couponCodeTxt=(AutoCompleteTextView)findViewById(R.id.couponCode);
            minPurAmtTxt=(AutoCompleteTextView)findViewById(R.id.minPurAmt);
            maxDiscTxt=(AutoCompleteTextView)findViewById(R.id.maxDisc);
            discPercTxt=(AutoCompleteTextView)findViewById(R.id.discPerc);
            discPriceTxt=(AutoCompleteTextView)findViewById(R.id.discPrice);
            expDateeTxt=(DatePicker)findViewById(R.id.expDatee);
            expDateeTxt.setMinDate(System.currentTimeMillis() - 1000);


            upload_btn_inp=(Button)findViewById(R.id.upload_btn);

            upload_btn_inp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    couponCode=couponCodeTxt.getText().toString();
                    minPurAmt=minPurAmtTxt.getText().toString();
                    maxDisc=maxDiscTxt.getText().toString();
                    discPerc=discPercTxt.getText().toString();
                    discPrice=discPriceTxt.getText().toString();

                    int frm_day = expDateeTxt.getDayOfMonth();
                    int frm_month = expDateeTxt.getMonth() + 1;
                    int frm_year = expDateeTxt.getYear();
                    String dayTxt;
                    String monTxt;


                    if(frm_day<10){
                        dayTxt="0"+frm_day;
                    }
                    else {
                        dayTxt=frm_day+"";
                    }
                    if(frm_month<10){
                        monTxt="0"+frm_month;
                    }
                    else {
                        monTxt=frm_month+"";
                    }
                    expDatee=frm_year+"-"+monTxt+"-"+dayTxt;

                    boolean fill_all=true;
                    if(TextUtils.isEmpty(couponCode)){
                        fill_all=false;
                        couponCodeTxt.setError("Required");
                        couponCodeTxt.requestFocus();
                    }
                    if(TextUtils.isEmpty(minPurAmt)){
                        fill_all=false;
                        minPurAmtTxt.setError("Required");
                        minPurAmtTxt.requestFocus();
                    }
                    if(TextUtils.isEmpty(maxDisc)){
                        fill_all=false;
                        maxDiscTxt.setError("Required");
                        maxDiscTxt.requestFocus();
                    }
                    if(TextUtils.isEmpty(discPerc) && TextUtils.isEmpty(discPrice)){
                        fill_all=false;
                        discPriceTxt.setError("Required");
                        discPriceTxt.requestFocus();
                    }


                    if(fill_all){
                        String otp_nums = "http://luxscar.com/luxscar_app/newOffer.php?";
                        final String otp_url = otp_nums;
                        new LongOperation().execute(otp_url);
                    }else{
                        Snackbar.make(v,"Please Fill in all fields", Snackbar.LENGTH_LONG).show();
                    }





                }
            });


        }catch (Exception e){
            Log.v("net_err",e.getMessage());
        }


        // ivImage = (ImageView) findViewById(R.id.gy);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }


    public class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(AddOffer.this);
        String data ="";
        String otpt="";



        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)

            Dialog.setMessage("Please wait..");
            Dialog.show();



        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            /************ Make Post Call To Web Server ***********/
            BufferedReader reader=null;

            // Send data



            File[] all_fl=new File[50];

            for(int i=0;i<=tot_en;i++){
                Log.v("fl_nme",tot_files[i]);
                all_fl[i]=new File(tot_files[i]);
                if(all_fl[i].isFile()){
                    Log.v("fl_nm","avls"+tot_en);
                }else{
                    Log.v("fl_nm","not_avl");
                }
            }

            String urlString = "http://luxscar.com/luxscar_app/newOffer.php";
            try
            {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(urlString);
                FileBody[] fl_bd=new FileBody[50];

                for(int i=0;i<=tot_en;i++){
                    fl_bd[i]=new FileBody(all_fl[i]);
                }


                MultipartEntity reqEntity = new MultipartEntity();
                for(int i=0;i<=tot_en;i++){
                    reqEntity.addPart("uploaded_file"+i, fl_bd[i]);
                }
                reqEntity.addPart("jsk",new StringBody("ndjfhfjghfs"));
                reqEntity.addPart("couponCode",new StringBody(couponCode));
                reqEntity.addPart("minPurAmt",new StringBody(minPurAmt));
                reqEntity.addPart("maxDisc",new StringBody(maxDisc));
                reqEntity.addPart("discPerc",new StringBody(discPerc));
                reqEntity.addPart("discPrice",new StringBody(discPrice));
                reqEntity.addPart("expDate",new StringBody(expDatee));

                post.setEntity(reqEntity);
                HttpResponse response = client.execute(post);
                resEntity = response.getEntity();
                final String response_str = EntityUtils.toString(resEntity);
                if (resEntity != null) {
                    Log.i("RESPONSE_NET",response_str);
                    otpt=response_str;
                    runOnUiThread(new Runnable(){
                        public void run() {
                            try {
                                Toast.makeText(getApplicationContext(),response_str, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
            catch (Exception ex){
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }


            /*****************************************************/
            return null;
        }


        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.


            Dialog.dismiss();



            if(otpt.hashCode()==0){
                Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();

            }else{
                finish();
                Intent ints=new Intent(getApplicationContext(),AllOffers.class);
                startActivity(ints);

            }



        }

    }



}
