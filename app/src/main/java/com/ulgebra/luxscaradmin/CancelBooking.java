package com.ulgebra.luxscaradmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class CancelBooking extends AppCompatActivity {
String booking_idd,cancel_reason;
    public ArrayList<Car_lists> parents;
Spinner spinner;
    public ProgressDialog dialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentzc = getIntent();


         booking_idd= intentzc.getStringExtra("booking_idd");

        setContentView(R.layout.activity_cancel_booking);
        TextView bookingIdHoldr=(TextView) findViewById(R.id.bookingIdHoldr);
        bookingIdHoldr.setText("Booking Id #"+booking_idd+"");
         spinner = (Spinner) findViewById(R.id.cancel_reason);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cancel_reasons, R.layout.single_spin_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        //final String otp_url = cancelBookinUrl;
        Button confirmCancelBtn=(Button) findViewById(R.id.confirmCancelBtn);
        confirmCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                spinner = (Spinner) findViewById(R.id.cancel_reason);
                cancel_reason=spinner.getSelectedItem().toString();
                try {
                    cancel_reason= URLEncoder.encode(cancel_reason, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String dlt_url="http://luxscar.com/luxscar_app/cancelBooking.php?booking_id="+booking_idd+"&reason="+cancel_reason;
                Log.v("net_st",dlt_url);
                DeltOperation deltOperation=new DeltOperation();
                deltOperation.execute(dlt_url);


            }

        });


    }

    public class DeltOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        String data ="";
        String otpt="";



        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)
            dialog = new ProgressDialog(CancelBooking.this);
            dialog.setMessage("Please wait...");
            dialog.show();



        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            /************ Make Post Call To Web Server ***********/
            BufferedReader reader=null;

            // Send data
            try
            {
                // Defined URL  where to send data
                URL url = new URL(urls[0]);

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( "" );
                wr.flush();
                // Get the server response
                try{
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    // StringBuilder sb = new StringBuilder();
                    String line = null;


                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        // Append server response in string
                        // sb.append(line + "\n");
                        otpt+=line;

                    }

                    // Append Server Response To Content String
                    Content = otpt;
                    Log.i("my_err","output="+otpt);
                }catch (Exception e){

                }



            }
            catch(Exception ex)
            {
                Error = ex.getMessage();
            }
            finally
            {
                try
                {

                    reader.close();
                }

                catch(Exception ex) {}
            }

            /*****************************************************/
            return null;
        }


        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            dialog.dismiss();


            if(otpt.hashCode()==("Successfully Cancelled").hashCode()){
                Toast.makeText(getApplicationContext(),"Successfully Cancelled Your Booking",Toast.LENGTH_LONG).show();


            }else {

                if(otpt.hashCode()==0){
                    Toast.makeText(getApplicationContext(),"Check your Internet Connection",Toast.LENGTH_LONG).show();

                }else{

                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("needTab",1);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(),otpt,Toast.LENGTH_LONG).show();

                }
            }

        }

    }


}
