package com.example.j.crop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.lang.reflect.Method;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by J on 4/28/2018.
 */


    public class FetchPhoto extends AppCompatActivity implements PhotoRecycler.OnItemClickListener{

        private RecyclerView myRecyclerView;
        private PhotoRecycler myRecyclerViewAdapter;
        private LinearLayoutManager linearLayoutManager;
        ImageView IMG;

        TextView textInfo;

        /*image orientation*/
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.photo_recycler);


           // textInfo = (TextView)findViewById(R.id.info);
           // textInfo.setMovementMethod(new ScrollingMovementMethod());

            myRecyclerView = (RecyclerView)findViewById(R.id.myrecyclerview);
            linearLayoutManager =
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            myRecyclerViewAdapter = new PhotoRecycler(this, this);
            myRecyclerViewAdapter.setOnItemClickListener(this);
            myRecyclerView.setAdapter(myRecyclerViewAdapter);
            myRecyclerView.setLayoutManager(linearLayoutManager);
            IMG = (ImageView) findViewById(R.id.img);


            if(Build.VERSION.SDK_INT>=24){
                try{
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            //check for permission
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                //ask for permission
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1313);
            }

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
                galleryAddPic(imageFile.getAbsolutePath().toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, 1313);
            }
        });

            prepareGallery();
        }

        private void prepareGallery(){

            String ExternalStorageDirectoryPath = Environment
                    .getExternalStorageDirectory()
                    .getAbsolutePath();

            String targetPath = ExternalStorageDirectoryPath + "/DCIM/Camera";


            Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
            File targetDirectory = new File(targetPath);


            File[] files = targetDirectory.listFiles();
            for (File file : files){
                Uri uri = Uri.fromFile(file);
                myRecyclerViewAdapter.add(
                        myRecyclerViewAdapter.getItemCount(),
                        uri);
                //todo add photo to database
            }
        }

        @Override
        public void onItemClick(PhotoRecycler.ItemHolder item, int position) {

            String stringitemUri = item.getItemUri();

            /*display the selected image in a bigger view*/
            File imageFile = new File(stringitemUri);

            /*required for android 6.0+ for gallery permission*/
            String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
                //pickImageFromGallery();
            } else {
                EasyPermissions.requestPermissions(this, "Access for storage",
                        101, galleryPermissions);
            }
            final Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath()); //retrieving file
            Bitmap workingBitmap = Bitmap.createBitmap(myBitmap);
            final Bitmap trueCopy = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
            IMG.setImageBitmap(trueCopy);


            Toast.makeText(FetchPhoto.this, stringitemUri, Toast.LENGTH_SHORT).show();
        }


/* camera functions */

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
        return "image" + 0 + ".jpg";
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) //operation was successful
        {
            if (requestCode == 1313) //then we're taking a new photo
            {
                finish();
                Intent intent = new Intent(FetchPhoto.this, FetchPhoto.class);
                startActivity(intent); //restart the activity with the new photo in the list
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

