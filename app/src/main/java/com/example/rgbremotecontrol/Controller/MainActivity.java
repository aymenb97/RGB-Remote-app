package com.example.rgbremotecontrol.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.rgbremotecontrol.Model.RemoteCode;
import com.example.rgbremotecontrol.Model.RemoteCodes;
import com.example.rgbremotecontrol.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.hardware.ConsumerIrManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private List<RemoteCode> fRemoteCodes = new ArrayList<>();
    private RemoteCode mRemoteCode;
    private Button mBrUp;
    private Button mBrDown;
    private Button mOn;
    private Button mOff;
    private ConsumerIrManager mCIR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCIR = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);
        setContentView(R.layout.activity_main);
        mBrUp = (Button) findViewById(R.id.bUp);
        mBrDown = (Button) findViewById(R.id.bDown);
        mOn = (Button) findViewById(R.id.bOn);
        mOff = (Button) findViewById(R.id.bOff);
        mBrUp.setTag(0);
        mBrDown.setTag(1);
        mOff.setTag(2);
        mOn.setTag(3);
        mBrUp.setOnClickListener(this);
        mBrDown.setOnClickListener(this);
        mOn.setOnClickListener(this);
        mOff.setOnClickListener(this);
        try {
            JSONObject mObj = new JSONObject(loadIrFile());
            JSONArray m_jArray = mObj.getJSONArray("buttons");
            for (int i = 0; i < m_jArray.length(); i++) {
                JSONObject jo_inside = m_jArray.getJSONObject(i);
                JSONArray j_patterns = jo_inside.optJSONArray("pattern");
                int[] mCodes = new int[j_patterns.length()];

                for (int j = 0; j < j_patterns.length(); j++) {
                    mCodes[j] = j_patterns.optInt(j);
                    //("PATTERNS", Integer.toString(j_patterns.optInt(j)));
                }
                fRemoteCodes.add(new RemoteCode(mCodes, jo_inside.getString("name"), i));
                //Log.d("OBJECTS", jo_inside.getString("name"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String loadIrFile() {
        String json = null;
        try {
            InputStream is = MainActivity.this.getAssets().open("IRCodes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onClick(View v) {
        mCIR.transmit(38000, fRemoteCodes.get((int) v.getTag()).getRemotePattern());
    }
}
