package com.example.i861944.gcpml;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tejashs on 7/24/17.
 */

public class FlowerDetail extends DialogFragment implements View.OnClickListener{
    private JSONObject flowerDetails;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.flower_details_layout, container, false);
        getDialog().setTitle("Flower Details");
        Button okButton = (Button) rootView.findViewById(R.id.okButton);
        okButton.setOnClickListener(this);
        setDetailsInView(rootView);
        return rootView;
    }

    public void setFlowerDetails(JSONObject details){
        flowerDetails = details;
    }

    private void setDetailsInView(View view){
        try {
            TextView fwType = (TextView) view.findViewById(R.id.flowerType);
            String type = flowerDetails.getString("objectType");
            fwType.setText(type);

            TextView scName = (TextView) view.findViewById(R.id.scientificName);
            String scientificName = flowerDetails.getString("scienceName");
            scName.setText(scientificName);

            TextView classification = (TextView) view.findViewById(R.id.classification);
            String classificationTxt = flowerDetails.getString("classification");
            classification.setText(classificationTxt);

            TextView description = (TextView) view.findViewById(R.id.description);
            String desc = flowerDetails.getString("description");
            description.setText(desc);

            TextView wiki = (TextView) view.findViewById(R.id.wikilink);
            String wikilink = flowerDetails.getString("wikiLink");
            wiki.setText(wikilink);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
