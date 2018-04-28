package com.example.j.crop;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Created by J on 4/9/2018.
 * (AddNoteActivity)
 */

public class AddNote extends AppCompatActivity {

    static int x;
    static int y;
    static int watered;

    /*database*/
    private AppDatabase plantDatabase;
    private Note note;
    private boolean update;
    private boolean firstNoteCreated = false;
    //private boolean isWatered = false;


    private TextInputEditText et_title, et_content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        /*the coordinates for the query*/
        Bundle intent = getIntent().getExtras();
        x = intent.getInt("x");
        y = intent.getInt("y");
        watered = intent.getInt("watered"); //grabbing the "if watered" value here

    et_title = findViewById(R.id.et_title);
    et_content = findViewById(R.id.et_content);

    plantDatabase = AppDatabase.getInstance(AddNote.this);

    final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "plantdb.db").build();


    Button button = findViewById(R.id.but_save);

        if ( (note = (Note) getIntent().getSerializableExtra("note"))!=null && watered == 0 ) { // added the "&&" so that the program doesn't
                                                                                                        //think the watered is for a update
            getSupportActionBar().setTitle("Update Note");
            update = true;
            button.setText("Update");
            et_title.setText(note.getTitle());
            et_content.setText(note.getContent());
        }

    //upload note
        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // fetch data and create note object


            if (update) {
                note.setContent(et_content.getText().toString());
                note.setTitle(et_title.getText().toString());

                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        // code goes here.
                        db.databaseFunc().updateNote(note);
                    }
                });
                thread.start();
                setResult(note, 2);

            }

            else if(watered == 1){
                Date currentTime = Calendar.getInstance().getTime();
                note = new Note(currentTime.toString(), "plant been watered");
                new InsertTask(AddNote.this, note).execute();
                firstNoteCreated = true;
                Thread thread = new Thread(new Runnable() {
                    public void run() { //do this every time so that the plant_id is saved
                        // code goes here.
                        db.databaseFunc().updateNote(note);
                    }
                });
                thread.start();
            }

            else {
                note = new Note(et_content.getText().toString(), et_title.getText().toString());
                new InsertTask(AddNote.this, note).execute();
                firstNoteCreated = true;
                Thread thread = new Thread(new Runnable() {
                    public void run() { //do this every time so that the plant_id is saved
                        // code goes here.
                        db.databaseFunc().updateNote(note);
                    }
                });
                thread.start();
            }
            }
        }
        );
    }


    private void setResult(Note note, int flag){
        setResult(flag,new Intent().putExtra("note",note));
        finish();
    }

static class InsertTask extends AsyncTask<Void, Void, Boolean> {

    private WeakReference<AddNote> activityReference;
    private Note note;

    // only retain a weak reference to the activity
    InsertTask(AddNote context, Note note) {
        activityReference = new WeakReference<>(context);
        this.note = note;
    }

    // doInBackground methods runs on a worker thread
    @Override
    protected Boolean doInBackground(Void... objs) {
        long j = activityReference.get().plantDatabase.databaseFunc().insertNote(note);
        note.setNote_id(j);
        long p_id = activityReference.get().plantDatabase.databaseFunc().getPlantId(x,y);
        note.setPlant_id(p_id);

        //Log.e("ID ", "doInBackground: "+j );
        return true;
    }

    // onPostExecute runs on main thread
    @Override
    protected void onPostExecute(Boolean bool) {
        if (bool) {
            activityReference.get().setResult(note, 1);
            activityReference.get().finish();
        }
    }
}
}