package com.example.j.crop;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.j.crop.AppDatabase;

/**
 * Created by J on 3/30/2018.
 * https://www.pluralsight.com/guides/android/making-a-notes-app-using-room-database
 */

public class PhotoViewer extends AppCompatActivity {

    private int x, y = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Bundle intent = getIntent().getExtras();

        x = intent.getInt("x");
        y = intent.getInt("y");

        Button startCamera = (Button) findViewById(R.id.upload);

        startCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//todo photo code
                /*
                Intent intent = new Intent(PhotoViewer.this, AddNoteActivity.class);

                intent.putExtra("x", x);
                intent.putExtra("y", y);

                //Log.e("PLANTNOTES   ", "testing x and y: "+x + " " +y + "" ); //thats working

                startActivity(intent);
                */

            }
        });

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
    }
}
