package com.example.j.crop;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.j.crop.Plant;
import com.example.j.crop.DatabaseFunctions;

/**
 * Created by lhn41 on 4/2/2018.
 */
@Database(entities = {Plant.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    private static Builder<AppDatabase> INSTANCE;

    public abstract DatabaseFunctions databaseFunc();

    public static Builder<AppDatabase> getAppDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                       AppDatabase.class, "plant-database");

        }
        return INSTANCE;
    }

    public static void destoryInstance(){
        INSTANCE = null;
    }

}
