package com.example.j.crop.Photos;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by J on 4/27/2018.
 */

@Entity(tableName ="photo")
public class Photo implements Serializable {

    @ColumnInfo(name = "path")
    private String path;

    private long plant_id;

    private int x;
    private int y;

    @PrimaryKey(autoGenerate = true)
    private long photo_id;

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

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

    @Override
    public String toString() {
        return "Photo{" +
                "photo_id=" + photo_id +
                ", plant_id=" + plant_id +
                ", path=" + path +
                '}';
    }
}
