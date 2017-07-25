package com.example.i861944.gcpml;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import org.json.JSONException;
import org.json.JSONObject;

import static android.graphics.BitmapFactory.decodeFile;

public class LandingPage extends AppCompatActivity implements Callback{
    private static final String BASE_URL = "https://saphxe-168717.appspot.com/predict/";
//    private static final String BASE_URL = "http://10.0.2.2:5000/predict/";
    private static final String FLOWER = "flower";
    private static final String NUMBER = "number";
    private Uri imageUri;
    private int objectType;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private String mCurrentPhotoPath;
    private static final String CAMERA_DIR = "/dcim/";
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    public File getAlbumStorageDir(String albumName) {
        return new File (
                Environment.getExternalStorageDirectory()
                        + CAMERA_DIR
                        + albumName
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Button predictBtn = (Button) findViewById(R.id.button3);
        predictBtn.setVisibility(View.GONE);
        imageUri = null;
        ImageView myImageView = (ImageView) findViewById(R.id.imageView2);
        myImageView.setVisibility(View.GONE);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    private String getUrl(String type){
        return BASE_URL + type;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            if (ContextCompat.checkSelfPermission(this, // request permission when it is not granted.
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.d("myAppName", "permission:WRITE_EXTERNAL_STORAGE: NOT granted!");
                ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            storageDir = getAlbumStorageDir("CameraSample");

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    public void openCamera(int code){

        final CharSequence[] items = { "Camera", "Gallery",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {

                    File f = null;

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    try {
                        f = setUpPhotoFile();
                        mCurrentPhotoPath = f.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    } catch (IOException e) {
                        e.printStackTrace();
                        f = null;
                        mCurrentPhotoPath = null;
                    }

                    startActivityForResult(takePictureIntent, REQUEST_CAMERA);

                } else if (items[item].equals("Gallery")) {
                    Intent galleryIntent = new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), SELECT_FILE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void uploadFlowerImage(View v){
        objectType=1;
        openCamera(1);
    }

    public void uploadNumberImage(View v){
        objectType=0;
        openCamera(0);
    }

    public void doPrediction(View v){
            uploadFileAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = null;

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                uri = data.getData();
            } else {
                File f = new File(mCurrentPhotoPath);
                uri = Uri.fromFile(f);
            }
        }
        //Uri uri = FileProvider.getUriForFile(this.getBaseContext(), this.getApplicationContext().getPackageName() + ".my.package.name.provider", f);
        ImageView myImageView = (ImageView) findViewById(R.id.imageView2);
        myImageView.setImageURI(uri);
        myImageView.setVisibility(View.VISIBLE);
        Button predictBtn = (Button) findViewById(R.id.button3);
        predictBtn.setVisibility(View.VISIBLE);
        imageUri = uri;
    }

    private void uploadFileAsync(){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            //BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            //Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
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
            client.post(this, url, params);
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

    @Override
    public void returnSuccess(JSONObject json) throws JSONException {
        String category = json.getString("objectCategory");
        String type = json.getString("objectType");
        String message = "Predicted " + category + " is " + type;
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        FragmentManager fm = getFragmentManager();
        FlowerDetail dialogFragment = new FlowerDetail ();
        dialogFragment.setFlowerDetails(json);
        dialogFragment.show(fm, "Sample Fragment");

    }
    @Override
    public void returnFailure(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

}
