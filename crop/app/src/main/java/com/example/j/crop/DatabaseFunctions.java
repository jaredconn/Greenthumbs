package com.example.j.crop;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by lhn41 on 4/2/2018.
 */
@Dao
public interface DatabaseFunctions {
    @Query("SELECT * FROM plant")
    List<Plant> getAll();

    @Query("SELECT * FROM notes")
    List<Note> getNotes();

    @Insert
    long insertPlant(Plant plant);

    @Insert
    long insertNote(Note notes);

    @Update
    void updateNote(Note repos);

    @Delete
    void deleteNote(Note note);

    @Delete
    void delete(Plant plant);

    @Query("SELECT * FROM notes INNER JOIN plant_join_note ON notes.note_id=plant_join_note.note_id WHERE plant_join_note.plant_id=:plant_id")
    List<Note> getNotesForPlant(final long plant_id);

    @Query("SELECT plant_id from plant WHERE x=:x AND y=:y")
    long getPlantId(int x, int y);
}
