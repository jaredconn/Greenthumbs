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
    private int plant_id;

    @ColumnInfo(name = "plantName")
    private String plant_name;

    private int note_id;

    /*private String plantStrain;
    private Bitmap plantPic;
    private String plantNotes;*/

    public void setPlant_id(int id)
    {
        plant_id = id;
    }

    public void setPlant_name(String name)
    {
        plant_name = name;
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

    public int getPlant_id()
    {
        return plant_id;
    }

    public String getPlant_name()
    {
        return plant_name;
    }

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
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
