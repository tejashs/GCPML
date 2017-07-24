package com.example.i861944.gcpml;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tejashs on 7/24/17.
 */

public interface Callback {

    public void returnSuccess(JSONObject response) throws JSONException;
    public void returnFailure(String errorMessage);
}
