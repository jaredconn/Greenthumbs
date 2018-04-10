package com.example.j.crop;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.VisibleForTesting;

import static android.arch.persistence.room.ForeignKey.CASCADE;
/**
 * Created by LamNg on 4/9/2018.
 */
@Entity(foreignKeys = @ForeignKey(entity = Plant.class,
parentColumns = "plantId",
childColumns = "plantId",
onDelete = CASCADE))

public class PhotoTable {
    @PrimaryKey(autoGenerate = true)
    private int photoId;

    @ColumnInfo(name = "photoPath")
    private String photoPath;

    private int plantId;

    public void setPhotoPath(String path)
    {
        photoPath = path;
    }

    public String getPhotoPath()
    {
        return photoPath;
    }

}
