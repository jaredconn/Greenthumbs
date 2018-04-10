package com.example.j.crop;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

/**
 * Created by LamNg on 4/9/2018.
 */
@Dao
public interface PhotoDAO {
    @Query("SELECT photoPath FROM phototable")
    String getPhotoPath();

    @Insert
    void insert(PhotoTable photo);

    @Update
    void update(PhotoTable photo);

    @Delete
    void delete(PhotoTable photo);
}
