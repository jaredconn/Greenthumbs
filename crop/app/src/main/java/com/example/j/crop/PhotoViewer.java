package com.example.j.crop;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.j.crop.AppDatabase;

/**
 * Created by J on 3/30/2018.
 * https://www.pluralsight.com/guides/android/making-a-notes-app-using-room-database
 */

public class PhotoViewer extends AppCompatActivity {

    //ensures the photos are taken and stored in natural orientation
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private int x, y = 0;
    int i = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);


        //allows camera access for android 7 deivces
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }


        Bundle intent = getIntent().getExtras();

        x = intent.getInt("x");
        y = intent.getInt("y");

        Button startCamera = (Button) findViewById(R.id.upload);
        Button exit = (Button) findViewById(R.id.exit);


        exit.setOnClickListener(new View.OnClickListener() { //go back to plots
            public void onClick(View v)
            {
                finish();
            }
        });

        startCamera.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v)
            {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File imageDirectory = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/" ); //gallery directory
                String fileName = getFileName();
                File imageFile = new File(imageDirectory, fileName);
                Uri imageUri = Uri.fromFile(imageFile);
                galleryAddPic("storage/emulated/0/DCIM/Camera/image" + 0 + ".jpg");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, 1313);
            }
        });
    }
    /**
     * Saves the taken photo to the gallery
     * @param mCurrentPhotoPath
     */
    protected void galleryAddPic(String mCurrentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);

      //todo ON RETREIVAL OF THE IMAGES, MAKE SURE TO FIX THE Y AXIS
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);


        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }



    private String getFileName() {
        i++;
        return "image" + i + ".jpg";
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) //operation was successful
        {
            if (requestCode == 1313) //then we're taking a new photo
            {
                //todo ON COMPLETION OF TAKING THE PHOTO, FINISH THE CURRENT PHOTOVIEWER.JAVA ACTIVITY THEN START A NEW INSTANCE OF PHOTOVIEWER.JAVA WITH THAT PHOTO IN THE HORIZONTAL VIEW.
                /*
                Intent intent = new Intent(this, PhotoViewer.class); //defining which activity to start next
                String path = "storage/emulated/0/DCIM/Camera/image" + 0 + ".jpg";
                intent.putExtra("path", path); //passing the file name so we know which file to open in the next activity
                startActivity(intent); // starting next activity
                */
            }
        }
        }




        //display the current time
        /*
       final TextView tv = (TextView) findViewById(R.id.tv);
        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv.setText(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.US).format(new Date()));
                someHandler.postDelayed(this, 1000);
            }
        }, 10);
        */
    }

