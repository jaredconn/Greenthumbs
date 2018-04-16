package com.example.j.crop;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by J on 4/8/2018.
 * (NoteListActivity)
 */

public class AddNoteActivity extends AppCompatActivity implements NotesAdapter.OnNoteItemClick{


    private TextView textViewMsg;
    private RecyclerView recyclerView;
    private AppDatabase noteDatabase;
    private List<Note> notes;
    private NotesAdapter notesAdapter;
    private int pos;

    private static int x;
    private static int y;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_recycler);

        Bundle intent = getIntent().getExtras();
        x = intent.getInt("plant_x");
        y = intent.getInt("plant_y");

        initializeVies();
        displayList();



    }

    private void displayList(){
        noteDatabase = AppDatabase.getInstance(AddNoteActivity.this);
        new RetrieveTask(this).execute();
    }

    private static class RetrieveTask extends AsyncTask<Void,Void,List<Note>>{

        private WeakReference<AddNoteActivity> activityReference;

        // only retain a weak reference to the activity
        RetrieveTask(AddNoteActivity context) {
            activityReference = new WeakReference<>(context);
        }


        //todo getNotesForPlant(long plant_id) ...get the specified notes for selected plant
        @Override
        protected List<Note> doInBackground(Void... voids) {
            if (activityReference.get()!=null)
                return activityReference.get().noteDatabase.databaseFunc().getNotesForPlant(activityReference.get().noteDatabase.databaseFunc().getPlantId(x,y));
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            if (notes!=null && notes.size()>0 ){
                activityReference.get().notes.clear();
                activityReference.get().notes.addAll(notes);
                // hides empty text view
                activityReference.get().textViewMsg.setVisibility(View.GONE);
                activityReference.get().notesAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initializeVies(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textViewMsg =  (TextView) findViewById(R.id.tv__empty);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(listener);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddNoteActivity.this));
        notes = new ArrayList<>();
        notesAdapter = new NotesAdapter(notes,AddNoteActivity.this);
        recyclerView.setAdapter(notesAdapter);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivityForResult(new Intent(AddNoteActivity.this, AddNote.class),100);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode > 0 ){
            if( resultCode == 1){
                notes.add((Note) data.getSerializableExtra("note"));
            }else if( resultCode == 2){
                notes.set(pos,(Note) data.getSerializableExtra("note"));
            }
            listVisibility();
        }
    }

    @Override
    public void onNoteClick(final int pos) {
        new AlertDialog.Builder(AddNoteActivity.this)
                .setTitle("Select Options")
                .setItems(new String[]{"Delete", "Update"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                noteDatabase.databaseFunc().deleteNote(notes.get(pos));
                                notes.remove(pos);
                                listVisibility();
                                break;
                            case 1:
                                AddNoteActivity.this.pos = pos;
                                startActivityForResult(
                                        new Intent(AddNoteActivity.this,
                                                AddNote.class).putExtra("note",notes.get(pos)),
                                        100);

                                break;
                        }
                    }
                }).show();

    }

    private void listVisibility(){
        int emptyMsgVisibility = View.GONE;
        if (notes.size() == 0){ // no item to display
            if (textViewMsg.getVisibility() == View.GONE)
                emptyMsgVisibility = View.VISIBLE;
        }
        textViewMsg.setVisibility(emptyMsgVisibility);
        notesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        noteDatabase.cleanUp();
        super.onDestroy();
    }
}

