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
    private static Callback handler;

    public static void post(Callback callbackHandler, String url, RequestParams params) {
        handler = callbackHandler;
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                try {
                    JSONObject json = new JSONObject(response);
                    handler.returnSuccess(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.returnFailure(response);
                }
                System.out.println("Success");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("Failed");
                handler.returnFailure("Prediction Failed");
//                Toast.makeText(parent, "Prediction Failed", Toast.LENGTH_LONG).show();
            }
        });
    }


}
