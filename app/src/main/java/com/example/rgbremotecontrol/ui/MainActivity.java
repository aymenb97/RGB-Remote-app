package com.example.rgbremotecontrol.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.rgbremotecontrol.Model.RemoteCode;
import com.example.rgbremotecontrol.R;
import com.example.rgbremotecontrol.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.hardware.ConsumerIrManager;

import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    private List<RemoteCode> fRemoteCodes = new ArrayList<>();
    private RemoteCode mRemoteCode;
    private ConsumerIrManager mCIR;
    private TextView mText;
    private LinearLayout mContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCIR = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewmodel(this);
        mText = (TextView) findViewById(R.id.ir_support);
        mContent = (LinearLayout) findViewById(R.id.Content);
        mText.setVisibility(View.VISIBLE);
        mContent.setVisibility(View.VISIBLE);


        if (mCIR.hasIrEmitter()) {
            mContent.setVisibility(View.VISIBLE);
            mText.setVisibility(View.GONE);
        } else {
            mText.setVisibility(View.VISIBLE);
            mContent.setVisibility(View.GONE);
        }
        try {
            JSONObject mObj = new JSONObject(loadIrFile());
            JSONArray m_jArray = mObj.getJSONArray("buttons");
            for (int i = 0; i < m_jArray.length(); i++) {
                JSONObject jo_inside = m_jArray.getJSONObject(i);
                JSONArray j_patterns = jo_inside.optJSONArray("pattern");
                int[] mCodes = new int[j_patterns.length()];

                for (int j = 0; j < j_patterns.length(); j++) {
                    mCodes[j] = j_patterns.optInt(j);

                }
                fRemoteCodes.add(new RemoteCode(mCodes, jo_inside.getString("name"), i));


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


    public void emitIR(View v, int index) {
        mCIR.transmit(38000, fRemoteCodes.get(index).getRemotePattern());
    }


}
