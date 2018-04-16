package com.example.j.crop;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

/**
 * Created by J on 4/15/2018.
 */

    @Entity(tableName = "plant_join_note",
            primaryKeys = { "plant_id", "note_id" },
            foreignKeys = {
                    @ForeignKey(entity = Plant.class,
                            parentColumns = "plant_id",
                            childColumns = "plant_id"),
                    @ForeignKey(entity = Note.class,
                            parentColumns = "note_id",
                            childColumns = "note_id")
            })
    public class PlantJoinNote {
        public final int plant_id;
        public final int note_id;

        public PlantJoinNote(final int plant_id, final int note_id) {
            this.plant_id = plant_id;
            this.note_id = note_id;
        }
    }
