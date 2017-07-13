package com.example.i861944.gcpml;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.*;

public class LandingPage extends AppCompatActivity {
    private static final String BASE_URL = "https://saphxe-168717.appspot.com/predict/";
    private static final String FLOWER = "flower";
    private static final String NUMBER = "number";
    private Uri imageUri;
    private int objectType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Button predictBtn = (Button) findViewById(R.id.button3);
        predictBtn.setVisibility(View.GONE);
        imageUri = null;
        ImageView myImageView = (ImageView) findViewById(R.id.imageView2);
        myImageView.setVisibility(View.GONE);
    }

    private String getUrl(String type){
        return BASE_URL + type;
    }

    public void openCamera(int code){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), code);
    }

    public void uploadFlowerImage(View v){
        openCamera(1);
    }

    public void uploadNumberImage(View v){
        openCamera(0);
    }

    public void doPrediction(View v){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            String strImage = getStringImage(bitmap);
            //volleyImageUpload(strImage);
            uploadFileAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = data.getData();
        ImageView myImageView = (ImageView) findViewById(R.id.imageView2);
        myImageView.setImageURI(uri);
        myImageView.setVisibility(View.VISIBLE);
        Button predictBtn = (Button) findViewById(R.id.button3);
        predictBtn.setVisibility(View.VISIBLE);
        imageUri = uri;
        objectType = requestCode;
    }

    private void uploadFileAsync(){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            byte[] imageBytes = getByteArray(bitmap);
            RequestParams params = new RequestParams();
            String url = null;
            if(objectType == 0){
                //Number
                params.put("digitImage", new ByteArrayInputStream(imageBytes), "myNewFlowerImage.jpg");
                url = getUrl(NUMBER);
            }
            else {
                //Flower
                params.put("flowerImage", new ByteArrayInputStream(imageBytes), "myNewFlowerImage.jpg");
                url = getUrl(FLOWER);
            }

            MyHttpClient client = new MyHttpClient();
            client.post(url, this, params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public byte[] getByteArray(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return imageBytes;
    }

//    public void volleyImageUpload(final String stringImage){
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        //Disimissing the progress dialog
//                        //Showing toast message of the response
//                        Toast.makeText(LandingPage.this, "Upload Flower activity Successful", Toast.LENGTH_SHORT).show();
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(LandingPage.this, "Upload Flower activity Failed", Toast.LENGTH_SHORT).show();
//
//                    }
//                }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                //Converting Bitmap to String
//                String image = stringImage;
//
//                //Getting Image Name
//                String name = "BlahflowerImageName";
//
//                //Creating parameters
//                Map<String,String> params = new Hashtable<String, String>();
//
//                //Adding parameters
//                params.put("flowerImage", image);
//                params.put("name", name);
//
//                //returning parameters
//                return params;
//            }
//        };
//
//        //Creating a Request Queue
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        //Adding request to the queue
//        requestQueue.add(stringRequest);
//    }


}
