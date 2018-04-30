package com.example.j.crop;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by J on 4/27/2018.
 */

@Entity(tableName ="photo")
public class Photo implements Serializable {

    @ColumnInfo(name = "Photo_content")

    private String path;
    private long plant_id;
    private int plant_x;
    private int plant_y;

    @PrimaryKey(autoGenerate = true)
    private long photo_id;

    public void setPhoto_id(long photo_id) {
            this.photo_id = photo_id;
    }

    public long getPhoto_id() {
        return photo_id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(long plant_id) {
        this.plant_id = plant_id;
    }

    public int getPlant_x() {
        return plant_x;
    }

    public void setPlant_x(int plant_x) {
        this.plant_x = plant_x;
    }

    public int getPlant_y() {
        return plant_y;
    }

    public void setPlant_y(int plant_y) {
        this.plant_y = plant_y;
    }
}
