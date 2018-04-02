package com.example.j.crop;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

/**
 * Created by lhn41 on 4/2/2018.
 */

public class DatabaseInitializer {
    private static final String TAG = DatabaseInitializer.class.getName();

    public static void populateAsync(@NonNull final AppDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }

    private static Plant addPlant(final AppDatabase db, Plant plant) {
        db.databaseFunc().insertPlant(plant);
        return plant;
    }

    private static void populateWithTestData(AppDatabase db) {
        Plant plant = new Plant();
        plant.setPlantID(11);
        plant.setPlantName("Weed");
        addPlant(db, plant);

        List<Plant> userList = db.databaseFunc().getAll();
        Log.d(DatabaseInitializer.TAG, "Rows Count: " + userList.size());
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }

    }
}
