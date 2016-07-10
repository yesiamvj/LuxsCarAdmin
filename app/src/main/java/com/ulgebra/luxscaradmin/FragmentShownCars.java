package com.ulgebra.luxscaradmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static java.lang.Boolean.TRUE;

public class FragmentShownCars extends Fragment {

    public ArrayList<Car_lists> parents;
    ListView listView;
    View rootView;
    ProgressDialog dialog;
    public DialogFragment Dialog;
    TextView from_to_inp;
    LongOperation longOperation=new LongOperation();
    private int ChildClickStatus=-1;

    LinearLayout linearLayout;

    String serverURL = "http://luxscar.com/luxscar_app/shown_cars.php";
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        try{
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            DialogFragment dialogFragment = new DialogFragment();

           // dialogFragment.show(ft,"Loading");
             dialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);
            rootView = inflater.inflate(R.layout.activity_admin_shown_cars, container, false);

            linearLayout=(LinearLayout)rootView.findViewById(R.id.list_hold);




            new LongOperation().execute(serverURL);




        }catch (Exception e){
            Log.v("net_err","err="+e.getMessage());

        }



       // listView=(ListView)rootView.findViewById(R.id.list_hold);

        return rootView;
        // Use AsyncTask execute Method To Prevent ANR Problem





    }

    public void goToSingleCarDetail(View view){

        Intent intent=new Intent(getActivity().getApplicationContext(),SingleCarDetail.class);
        startActivity(intent);
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


            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)
/*
            Dialog.setMessage("Please wait..");
            Dialog.show();

      */  }

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


        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            //Dialog.dismiss();
            dialog.dismiss();



            if (Error != null) {



            } else {



                // Show Response Json On Screen (activity)
                //uiUpdate.setText( Content );

                /****************** Start Parse Response JSON Data *************/

                String OutputData = "";
                JSONObject jsonResponse;
                final ArrayList<Car_lists> lists=new ArrayList<Car_lists>();


                try {

                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);

                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    /*******  Returns null otherwise.  *******/
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("Car_items");

                    /*********** Process each JSON Node ************/

                    int lengthJsonArr = jsonMainNode.length();

                    for(int i=0; i < lengthJsonArr; i++)
                    {
                        final Car_lists mp=new Car_lists();


                        /****** Get Object for each JSON node.***********/
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        /******* Fetch node values **********/
                        String name       = jsonChildNode.optString("car_name").toString();
                        String cost     = jsonChildNode.optString("cost").toString();
                        String c_image = jsonChildNode.optString("car_image").toString();
                        int car_id=jsonChildNode.optInt("car_id");
                        mp.set_carname(name);
                        mp.setCar_cost(cost);
                        mp.setCar_id(car_id);
                        mp.setCar_image(c_image);

                        lists.add(mp);



                        Log.i("net_err","tot_cnt="+i);
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
            if( ChildClickStatus!=position)
            {
                ChildClickStatus = position;


            }

            return position;
        }

        @Override

        public View getView(int groupPosition, View conView, ViewGroup parent) {

            final Car_lists my_parent = parents.get(groupPosition);

            LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
            conView = inflater.inflate(R.layout.sigle_car_container, parent,false);

            conView.setTag(h);
            h.car_name_inp = (TextView) conView.findViewById(R.id.car_name);
            h.cost_inp = (TextView) conView.findViewById(R.id.car_cost);
            h.selc_car_inp=(Button)conView.findViewById(R.id.selc_car);
            h.car_image_inp=(ImageView)conView.findViewById(R.id.car_image);
            h.car_hold=(LinearLayout)conView.findViewById(R.id.car_container);
            h.car_hold.setId(my_parent.getCar_id());

            h.s_car_cont.setTag(my_parent.getCar_id());
            h.car_hold.setTag(my_parent.getCar_id());
            h.selc_car_inp.setText("Hide Car");
            h.selc_car_inp.setBackgroundColor(Color.parseColor("#c0392b"));
            h.selc_car_inp.setTag(my_parent.getCar_id());
            h.selc_car_inp.setId(my_parent.getCar_id());
            h.s_car_cont=(LinearLayout)conView.findViewById(R.id.sigle_car);
            h.s_car_cont.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent ints=new Intent(getActivity().getApplicationContext(),SingleCarDetail.class);
                    ints.putExtra("car_id",my_parent.getCar_id());
                    startActivity(ints);


                }
            });
            h.selc_car_inp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    dialog = ProgressDialog.show(getActivity(), "Proccessing Please wait...", ""+v.getId(), true);
                    String hide_url="http://luxscar.com/luxscar_app/hide_car.php?car_idsuiru="+my_parent.getCar_id();
                    new HideOperation().execute(hide_url);










                }
            });

            h.car_name_inp.setText(my_parent.getCar_name());
            h.cost_inp.setText("RS "+my_parent.getCar_cost()+" / per day");


            new ImageLoadTask("http://luxscar.com/luxscar_app/"+my_parent.getCar_image(), h.car_image_inp).execute();

            Log.v("net_err","set ");

            return conView;

        }


        public class Holder {
            TextView car_name_inp,cost_inp,image_inp;
            Button selc_car_inp;
            ImageView car_image_inp;
            LinearLayout s_car_cont,car_hold;

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

    public class HideOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        String data ="";
        String otpt="";



        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)



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


            if(otpt.hashCode()==0){
                Toast.makeText(getActivity().getApplicationContext(),"Check your Internet Connection",Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getActivity().getApplicationContext(),otpt,Toast.LENGTH_LONG).show();

                this.cancel(true);

            }

        }

    }

    public void loadHosts(final ArrayList<Car_lists> newParents)
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
       // final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter();

        // Set Adapter to ExpandableList Adapter

        for(int i=0;i<parents.size();i++){
            final Car_lists my_parent = parents.get(i);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.sigle_car_container,null);
            TextView car_name=(TextView)view.findViewById(R.id.car_name);

            TextView car_cost=(TextView)view.findViewById(R.id.car_cost);

            Button selc_cars=(Button)view.findViewById(R.id.selc_car);

            car_name.setText(my_parent.getCar_name());

            car_cost.setText("RS. "+my_parent.getCar_cost()+" / per day");

            selc_cars.setText("Hide Car");
            selc_cars.setBackgroundColor(Color.parseColor("#c0392b"));

            LinearLayout car_holders=(LinearLayout)view.findViewById(R.id.sigle_car);
            car_holders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent ints=new Intent(getActivity().getApplicationContext(),SingleCarDetail.class);
                    ints.putExtra("car_id",my_parent.getCar_id());
                    startActivity(ints);


                }
            });
            selc_cars.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    dialog = ProgressDialog.show(getActivity(), "H...", ""+v.getId(), true);
                    String hide_url="http://luxscar.com/luxscar_app/hide_car.php?car_idsuiru="+my_parent.getCar_id();
                    new HideOperation().execute(hide_url);

                    try{
                        linearLayout.removeAllViews();
                        new LongOperation().execute(serverURL);


                        Log.v("long_op",serverURL);
                    }catch (Exception e){
                        Log.v("long_op",e.getMessage());
                    }





                }
            });

            ImageView imgs=(ImageView)view.findViewById(R.id.car_image);

            new ImageLoadTask("http://luxscar.com/luxscar_app/"+my_parent.getCar_image(), imgs).execute();

            linearLayout.addView(view);
        }

    }

}
