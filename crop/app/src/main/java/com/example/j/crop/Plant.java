package com.example.j.crop;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
/**import android.graphics.Bitmap;*/

/**
 * http://www.androiddeft.com/2017/05/06/android-storing-data-using-sqlite-database/
 * Bases on the link above.
 * Created by lhn41 on 3/28/2018.
 */
@Entity(tableName = "plant")
public class Plant
{
    @PrimaryKey(autoGenerate = true)
    private int plantID;

    @ColumnInfo(name = "plantName")
    private String plantName;

    /*private String plantStrain;
    private Bitmap plantPic;
    private String plantNotes;*/

    public void setPlantID(int id)
    {
        plantID = id;
    }

    public void setPlantName(String name)
    {
        plantName = name;
    }

    /*public void setPlantPic(Bitmap pic)
    {
        plantPic = pic;
    }

    public void setPlantStrain(String strain)
    {
        plantStrain = strain;
    }

    public void setPlantNotes(String notes)
    {
        plantNotes = notes;
    }*/

    public int getPlantID()
    {
        return plantID;
    }

    public String getPlantName()
    {
        return plantName;
    }

    /*public String getPlantStrain()
    {
        return plantStrain;
    }*/

    /*public String getPlantNotes()
    {
        return plantNotes;
    }*/

    /*public Bitmap getPlantPic()
    {
        return plantPic;
    }*/
}
