package com.ulgebra.luxscaradmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class SingleCarDetail extends AppCompatActivity {


    ProgressDialog dialog;
    String cars_name,cost,car_id,car_number;

    public ArrayList<Car_lists> parents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_car_detail);

        Intent intent = getIntent();
        int car_id_input=intent.getIntExtra("car_id",0);


        Log.v("net_res","car id="+car_id_input);
        String otp_nums = "http://luxscar.com/luxscar_app/single_car_dtl.php?car_id="+car_id_input;


        final String otp_url = otp_nums;
        new LongOperation().execute(otp_url);






    }
    private class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;

        String data ="";
        int sizeData = 0;

        // Log.v("net_err","dlg");
        protected void onPreExecute() {

        /*    dialog = ProgressDialog.show(getApplicationContext(), "Loading...", "Please wait...", true);

            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)
            dialog.show();
*/
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
                wr.write( data );
                wr.flush();

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + "\n");
                }

                // Append Server Response To Content String
                Content = sb.toString();
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

        private void loadHosts(final ArrayList<Car_lists> newParents)
        {
            if (newParents == null){
                Log.i("err", "returned");
                return;
            }else{
                Log.i("err","ok");
            }



            parents = newParents;
            Log.i("err","lv");
            // Check for ExpandableListAdapter object

            Log.i("err","fea");

            TextView car_name_inp=(TextView)findViewById(R.id.car_brand_name);
            car_name_inp.setText(cars_name);

            Log.v("net_res","car name="+cars_name);
            Log.v("net_res","car no="+car_number);
            Log.v("net_res","car cost="+cost);

            TextView cost_inp=(TextView)findViewById(R.id.car_cost);
            cost_inp.setText(cost);
            TextView caar_num_inp=(TextView)findViewById(R.id.car_number);
            caar_num_inp.setText(car_number);

           ListView listView=(ListView)findViewById(R.id.car_image_holder);
            final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter();

            // Set Adapter to ExpandableList Adapter
            listView.setAdapter(mAdapter);

        }
        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            //Dialog.dismiss();
         //   dialog.dismiss();



            if (Error != null) {



            } else {



                // Show Response Json On Screen (activity)
                //uiUpdate.setText( Content );

                /****************** Start Parse Response JSON Data *************/

                String OutputData = "";
                JSONObject jsonResponse;
                final ArrayList<Car_lists> lists=new ArrayList<Car_lists>();
                JSONArray jsonMainNode,jsonMainNode1;

                try {

                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);

                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    /*******  Returns null otherwise.  *******/

                    jsonMainNode1=jsonResponse.optJSONArray("Car_images");

                    int car_img_len=jsonMainNode1.length();

                    for(int i=0;i<car_img_len;i++){
                        final Car_lists mp=new Car_lists();


                        JSONObject jsonChildNode1 = jsonMainNode1.getJSONObject(i);

                        String car_image=jsonChildNode1.optString("car_image").toString();
                        mp.setCar_image(car_image);

                        Log.v("img_car",car_img_len+" "+car_image);
                        lists.add(mp);

                    }


                     jsonMainNode = jsonResponse.optJSONArray("Car_items");

                    /*********** Process each JSON Node ************/

                    int lengthJsonArr = jsonMainNode.length();


                    Log.v("net_res","json len="+lengthJsonArr);
                    for(int i=0; i < lengthJsonArr; i++)
                    {


                        /****** Get Object for each JSON node.***********/
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        /******* Fetch node values **********/
                        cars_name       = "defal";
                         cost     = jsonChildNode.optString("cost").toString();
                        car_number = jsonChildNode.optString("car_no").toString();
                         car_id=jsonChildNode.optString("car_id");






                    }

                    loadHosts(lists);

                    /****************** End Parse Response JSON Data *************/

                    //Show Parsed Output on screen (activity)


                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }

    }

    private class MyExpandableListAdapter extends BaseAdapter {

        Holder h = new Holder();
        @Override
        public int getCount() {


            return parents.size();


        }

        @Override
        public Object getItem(int position) {
            return parents.get(position);
        }

        @Override
        public long getItemId(int position) {
            //***** When Child row clicked then this function call ******

            //Log.i("Noise", "parent == "+groupPosition+"=  child : =="+childPosition);

            return position;
        }

        @Override

        public View getView(int groupPosition, View conView, ViewGroup parent) {

            final Car_lists my_parent = parents.get(groupPosition);

            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            conView = inflater.inflate(R.layout.single_car_img_detail, parent,false);

            conView.setTag(h);


                    h.car_image_inp=(ImageView)findViewById(R.id.single_dtl_img);

            new ImageLoadTask("http://luxscar.com/luxscar_app/"+my_parent.getCar_image(), h.car_image_inp);




            return conView;

        }


        public class Holder {

            ImageView car_image_inp;

        }
    }
    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }


}
