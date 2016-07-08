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

public class AddNewCar extends AppCompatActivity {

    AutoCompleteTextView model_inp,brand_name_inp,car_no_inp,cost_inp;
    String model_no,brand_name,car_no,cost,visible_status;
       Switch visible_switch_inp;
    int one_img=0;
    int one_img_sts=-1;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect;
    private ImageView ivImage;
    private String userChoosenTask;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
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
        setContentView(R.layout.activity_add_new_car);
        btnSelect = (Button) findViewById(R.id.hu);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        try{

            brand_name_inp=(AutoCompleteTextView)findViewById(R.id.addCar_brandName);
            model_inp=(AutoCompleteTextView)findViewById(R.id.addCar_modelName);
            car_no_inp=(AutoCompleteTextView)findViewById(R.id.car_no);
            cost_inp=(AutoCompleteTextView)findViewById(R.id.cost_per_day);
            visible_switch_inp=(Switch)findViewById(R.id.visible_switch);
            upload_btn_inp=(Button)findViewById(R.id.upload_btn);


            upload_btn_inp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(visible_switch_inp.isChecked()==true){
                        visible_status="checked";

                    }else{
                        visible_status="not checked";
                    }
                    model_no=model_inp.getText().toString();
                    brand_name=brand_name_inp.getText().toString();
                    car_no=car_no_inp.getText().toString();
                    cost=cost_inp.getText().toString();


                    boolean fill_all=true;
                    if(TextUtils.isEmpty(model_no)){
                        fill_all=false;
                        model_inp.setError("Required");
                        model_inp.requestFocus();
                    }
                    if(TextUtils.isEmpty(brand_name)){
                        fill_all=false;
                        brand_name_inp.setError("Required");
                        brand_name_inp.requestFocus();
                    }
                    if(TextUtils.isEmpty(car_no)){
                        fill_all=false;
                        car_no_inp.setError("Required");
                        car_no_inp.requestFocus();
                    }
                    if(TextUtils.isEmpty(cost)){
                        fill_all=false;
                        cost_inp.setError("Required");
                        cost_inp.requestFocus();
                    }


                    if(fill_all){
                        String otp_nums = "http://luxscar.com/luxscar_app/file_uploader.php?";


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
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewCar.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(AddNewCar.this);

                String userChoosenTask;
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent()
    {
        Log.v("cam_err","cam_strt");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select Files"),SELECT_FILE);
    }
    //    private void selectImage() {
//        final CharSequence[] items = { "Take Photo", "Choose from Library",
//                "Cancel" };
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewCar.this);
//        builder.setTitle("Add Photo!");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                boolean result=Utility.checkPermission(AddNewCar.this);
//
//                String userChoosenTask;
//                if (items[item].equals("Take Photo")) {
//                    userChoosenTask="Take Photo";
//                    if(result)
//                        cameraIntent();
//
//                } else if (items[item].equals("Choose from Library")) {
//                    userChoosenTask="Choose from Library";
//                    if(result)
//                        galleryIntent();
//
//                } else if (items[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }
//    private void cameraIntent()
//    {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQUEST_CAMERA);
//    }

    //  @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == SELECT_FILE)
//                onSelectFromGalleryResult(data);
//            else if (requestCode == REQUEST_CAMERA)
//                onCaptureImageResult(data);
//        }
//    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {


            // When an Image is picked
            if ( resultCode == Activity.RESULT_OK
                    && null != data) {
                if (requestCode == REQUEST_CAMERA)
                {
        one_img=1;
                    onCaptureImageResult(data);
                }else
                {

                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    imagesEncodedList = new ArrayList<String>();
                    if(data.getData()!=null){
        one_img=1;
                        Uri mImageUri=data.getData();
                        Log.v("LOG_TAG", "Selected Image URi" + mImageUri);
                        ImageView yourImage2 = (ImageView) findViewById(R.id.dc);
                        yourImage2.setImageURI(mImageUri);
                        compressImage(mImageUri.toString());
                        // Get the cursor
                        Cursor cursor = getContentResolver().query(mImageUri,
                                filePathColumn, null, null, null);

                        // Move to first row
                        cursor.moveToFirst();




                        //yourImage2.setImageURI(mImageUri);
                        cursor.close();

                    }else {
        one_img=0;
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                            LinearLayout layout = (LinearLayout) findViewById(R.id.ji);

                            for (int i = 0; i < mClipData.getItemCount(); i++) {


                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                Log.v("LOG_TAG", "Selected Image URi" + uri);

                                mArrayUri.add(uri);
                                LayoutInflater li = LayoutInflater.from(AddNewCar.this);
                                View customView = li.inflate(R.layout.image_cont,null);
                                ImageView yourImage = (ImageView) customView.findViewById(R.id.fi);
                                //compressImage(uri.toString());
                                yourImage.setImageBitmap(compressImage(uri.toString()));


                                int t = i + 1;



                                if(t<mClipData.getItemCount()){
                                    ClipData.Item item2 = mClipData.getItemAt(t);
                                    Uri uri2 = item2.getUri();
                                    ImageView yourImage2 = (ImageView) customView.findViewById(R.id.fi2);

                                    yourImage2.setImageBitmap(compressImage(uri2.toString()));
                                }

                                layout.addView(customView);




                                // Get the cursor
                                Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                                // Move to first row
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                imageEncoded  = cursor.getString(columnIndex);
                                imagesEncodedList.add(imageEncoded);
                                //Log.v("LOG_TAG", "Selected Image URi" + cursor.getColumnIndex(filePathColumn[0]));
                                //ivImage.setImageURI(cursor);
                                cursor.close();
                                if(t<mClipData.getItemCount()){
                                    i=i+1;
                                }

                            }
                            Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                        }
                    }
                }


            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // ivImage.setImageBitmap(bm);
    }
//    private void onCaptureImageResult(Intent data) {
//
//        Bundle extras = data.getExtras();
//        Bitmap imageBitmap = (Bitmap) extras.get("data");
//        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//       // thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//
//
//
//
//                LayoutInflater li4 = LayoutInflater.from(AddNewCar.this);
//                View customView4 = li4.inflate(R.layout.single_camera_image,null);
//                ImageView yourImage4 = (ImageView) customView4.findViewById(R.id.yu);
//                //compressImage(uri.toString());
//                yourImage4.setImageBitmap(imageBitmap);
//            }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void onCaptureImageResult(Intent data) {


        try{

            Bundle extras = data.getExtras();

            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView yourImage2 = (ImageView) findViewById(R.id.dc);
            Uri urils=getImageUri(this,imageBitmap);
            yourImage2.setImageURI(urils);
            compressImage(urils.toString());

        }catch (Exception e){
            Log.v("caam_err",e.getMessage());
        }
    }




    public Bitmap compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {               imgRatio = maxHeight / actualHeight;                actualWidth = (int) (imgRatio * actualWidth);               actualHeight = (int) maxHeight;             } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(!filename.contains("content://com.")){

            if(one_img==1){
                if(one_img_sts==-1){
                    tot_en=tot_en+1;

                    one_img_sts=tot_en;
                }

                tot_files[one_img_sts]=filename;
                tot_files[one_img_sts]=filename;
            }else {
                tot_en=tot_en+1;

                tot_files[tot_en]=filename;
            }

        }

        return scaledBitmap;

    }
    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }       final float totalPixels = width * height;       final float totalReqPixelsCap = reqWidth * reqHeight * 2;       while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(AddNewCar.this);
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

            String urlString = "http://luxscar.com/luxscar_app/file_uploader.php";
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
                reqEntity.addPart("brand_name",new StringBody(brand_name));
                reqEntity.addPart("model_name",new StringBody(model_no));
                reqEntity.addPart("car_no",new StringBody(car_no));
                reqEntity.addPart("cost_per_day",new StringBody(cost));
                reqEntity.addPart("visible_status",new StringBody(visible_status));


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
                Intent ints=new Intent(getApplicationContext(),AddNewCar.class);
                startActivity(ints);

            }



        }

    }



}
