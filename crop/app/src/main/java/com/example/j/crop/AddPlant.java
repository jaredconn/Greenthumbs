package com.example.j.crop;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.lang.ref.WeakReference;

/**
 * Created by J on 4/15/2018.
 */

public class AddPlant extends AppCompatActivity {

    private AppDatabase plantDatabase;
    private Plant plant;
    private boolean update;

   // private TextInputEditText et_title, et_content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_add_notes);


        plantDatabase = AppDatabase.getInstance(AddPlant.this);

      //  final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "plantdb.db").build();


        plant = new Plant();
        int x,y;

        Bundle intent = getIntent().getExtras();
        x = intent.getInt("x");
        y = intent.getInt("y");

        Log.e("AddPlant TESTTESTTEST ", "TESTING X AND Y: "+x + " "+  y );

        plant.setX(x);
        plant.setY(y);

        new AddPlant.InsertTask(AddPlant.this, plant).execute();

    }

    private void setResult(Plant plant, int flag){
        setResult(flag,new Intent().putExtra("plant", plant));
        finish();
    }

    private static class InsertTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<AddPlant> activityReference;
        private Plant plant;

        // only retain a weak reference to the activity
        InsertTask(AddPlant context, Plant plant) {
            activityReference = new WeakReference<>(context);
            this.plant = plant;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            long j = activityReference.get().plantDatabase.databaseFunc().insertPlant(plant);
            plant.setPlant_id(j);
            Log.e("Plant ID ", "doInBackground: "+j );
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool) {
                activityReference.get().setResult(plant, 1);
                activityReference.get().finish();
            }
        }
    }
}
