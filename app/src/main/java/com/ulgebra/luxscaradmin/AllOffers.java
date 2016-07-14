package com.ulgebra.luxscaradmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ulgebra.luxscaradmin.Car_lists;
import com.ulgebra.luxscaradmin.R;
import com.ulgebra.luxscaradmin.SingleBookingDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static android.os.ParcelFileDescriptor.MODE_WORLD_READABLE;

/**
 * Created by Vijayakumar on 08/07/2016.
 */

public class AllOffers extends AppCompatActivity {




    public ArrayList<Offers> parents;
    ListView listView;
    View rootView;
    ProgressDialog dialog;
    public DialogFragment Dialog;
    TextView from_to_inp;
    LongOperation longOperation=new LongOperation();
    private int ChildClickStatus=-1;
    String bookingStatus,cancelReason,cancelledOn,editedOn;

    LinearLayout linearLayout;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_all_offers);

        try{
//
            dialog = ProgressDialog.show(this, "Loading...", "Please wait...", true);

            linearLayout=(LinearLayout) findViewById(R.id.my_list_hold);



            String serverURL = "http://luxscar.com/luxscar_app/showOffersList.php";

            new LongOperation().execute(serverURL);




        }catch (Exception e){
            Log.v("net_err","err="+e.getMessage());

        }



        // listView=(ListView)rootView.findViewById(R.id.list_hold);


        // Use AsyncTask execute Method To Prevent ANR Problem





    }

    public class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        String data ="";
        String otpt="";



        protected void onPreExecute() {
            // NOTE: You can call UI Element here.




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
            try {
                JSONObject jsonResponse;
                Log.i("net_err", "try json");
                /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                jsonResponse = new JSONObject(Content);

                /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                /*******  Returns null otherwise.  *******/
                JSONArray jsonMainNode = jsonResponse.optJSONArray("Hstry_items");
                Log.i("net_err", "json main");
                /*********** Process each JSON Node ************/

                int lengthJsonArr = jsonMainNode.length();
                Log.i("net_err", lengthJsonArr+"is len");
                final ArrayList<Offers> lists=new ArrayList<Offers>();
                for(int i=0; i < lengthJsonArr; i++)
                {
                    final Offers mp=new Offers();


                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    /******* Fetch node values **********/
                    String username       = jsonChildNode.optString("coupon_code").toString();
                    String userMob     = jsonChildNode.optString("discPerc").toString();
                    String userId=jsonChildNode.optString("expiry_date").toString();
                    String offerId=jsonChildNode.optString("offer_id").toString();

                    Log.i("net_err", "user anem"+username+" mo"+userMob);


                    mp.setCoupon_code(username);
                    mp.setDiscPerc(userId);
                    mp.setExpiry_date(userMob);
                    mp.setOffer_id(offerId);

                    lists.add(mp);
                }
                Log.i("net_err", "out for json");

                loadHosts(lists);

            } catch (JSONException e) {

                e.printStackTrace();
                Log.i("net_err", "error json");
            }
            if(otpt.hashCode()==0){
                Toast.makeText(getApplicationContext(),"Check your Internet Connection",Toast.LENGTH_LONG).show();

            }else{
                // Toast.makeText(getActivity().getApplicationContext(),otpt,Toast.LENGTH_LONG).show();

            }


        }

    }
    public void loadHosts(final ArrayList<Offers> newParents)
    {
        if (newParents == null){
            Log.i("net_err", "lh returned");
            return;
        }else{
            Log.i("net_err","lh ok");
        }



        parents = newParents;


        for(int i=0;i<parents.size();i++){
            final Offers my_parent=parents.get(i);
            View vi= LayoutInflater.from(getApplicationContext()).inflate(R.layout.single_user,null);


            TextView booked_username=(TextView) vi.findViewById(R.id.bk_customer_name);
            TextView booked_userMob=(TextView) vi.findViewById(R.id.bk_customer_mob);

            LinearLayout holder_inp=(LinearLayout)vi.findViewById(R.id.single_user_hold);


            booked_username.setText(Html.fromHtml("<b>CODE : </b>"+my_parent.getCoupon_code()));
            booked_userMob.setText(Html.fromHtml("<b>Expiry Date : </b>"+my_parent.getDiscPerc()));


            holder_inp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),SingleOfferDetails.class);
                    intent.putExtra("user_idd",my_parent.getOffer_id());
                    startActivity(intent);
                }
            });

            linearLayout.addView(vi);

        }


    }

}
