package com.example.j.crop;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by J on 3/30/2018.
 */

public class PlantNotes extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);


        //display the current time
        final TextView tv = (TextView) findViewById(R.id.tv);
        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv.setText(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.US).format(new Date()));
                someHandler.postDelayed(this, 1000);
            }
        }, 10);


        final EditText et = (EditText) findViewById(R.id.editText);


    }
}