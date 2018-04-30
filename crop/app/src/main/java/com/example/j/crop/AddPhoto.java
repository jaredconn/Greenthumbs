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
 * Created by J on 4/27/2018.
 */

public class AddPhoto extends AppCompatActivity {

    private AppDatabase plantDatabase;
    private Photo photo;
    private static int x,y;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        plantDatabase = AppDatabase.getInstance(AddPhoto.this);

        photo = new Photo();

       // int x,y;
        String path;
        Bundle intent = getIntent().getExtras();
        path = intent.getString("path");
        x = intent.getInt("x");
        y = intent.getInt("y");

        Log.e("Photo Path ", "AddPhoto: "+ path );

        photo.setPath(path);
        photo.setPlant_x(x);
        photo.setPlant_y(y);

       // plant.setX(x);
        //plant.setY(y);

        new AddPhoto.InsertTask(AddPhoto.this, photo).execute();
    }

    private void setResult(Photo photo, int flag){
        setResult(flag,new Intent().putExtra("photo", photo));
        finish();
        Intent intent = new Intent(AddPhoto.this, FetchPhoto.class);
        intent.putExtra("x", x);
        intent.putExtra("y", y);
        startActivity(intent); //then go back to view mode
    }

    private static class InsertTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<AddPhoto> activityReference;
        private Photo photo;

        // only retain a weak reference to the activity
        InsertTask(AddPhoto context, Photo photo) {
            activityReference = new WeakReference<>(context);
            this.photo = photo;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            long j = activityReference.get().plantDatabase.databaseFunc().insertPhoto(photo);
            photo.setPhoto_id(j);
          //  photo.setPath(path);
            Log.e("Photo ID ", "doInBackground: "+j );
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool) {
                activityReference.get().setResult(photo, 1);
                activityReference.get().finish();
            }
        }
    }
}
