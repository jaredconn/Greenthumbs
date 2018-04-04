package com.example.j.crop;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by lhn41 on 4/2/2018.
 */
@Dao
public interface DatabaseFunctions {
    @Query("SELECT * FROM plant")
    List<Plant> getAll();

    @Insert
    void insertPlant(Plant plants);

    @Delete
    void delete(Plant plant);
}
