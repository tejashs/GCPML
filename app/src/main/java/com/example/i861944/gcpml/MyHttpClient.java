package com.example.i861944.gcpml;

/**
 * Created by tejashs on 7/11/17.
 */

import android.app.Activity;
import android.widget.Toast;

import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MyHttpClient {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void post(String url, final Activity parent, RequestParams params) {
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                try {
                    JSONObject json = new JSONObject(response);
                    String category = json.getString("objectCategory");
                    String type = json.getString("objectType");
                    String message = "Predicted " + category + " is " + type;
                    Toast.makeText(parent, message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(parent, response, Toast.LENGTH_LONG).show();
                }
                System.out.println("Success");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("Failed");
                Toast.makeText(parent, "Prediction Failed", Toast.LENGTH_LONG).show();
            }
        });
    }


}
