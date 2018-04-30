package com.example.j.crop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by J on 4/28/2018.
 */


    public class FetchPhoto extends AppCompatActivity implements PhotoRecycler.OnItemClickListener{

        private RecyclerView myRecyclerView;
        private PhotoRecycler myRecyclerViewAdapter;
        private LinearLayoutManager linearLayoutManager;
        private AppDatabase photoDatabase;
        ImageView IMG;
        Bitmap bitmap;
        String root;
        String fname;
        static int x;
    static int y;

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



            Bundle extras = getIntent().getExtras();
            x = extras.getInt("x");
            y = extras.getInt("y");

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

        IMG.setOnClickListener(new View.OnClickListener() { //make the image disappear on click
            @Override
            public void onClick(View v) {
               IMG.setVisibility(View.GONE);
            }
        });





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
                //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                File myDir = new File(root + "/Greenthumb");
                myDir.mkdirs();
                Random generator = new Random();
                int n = 1000000;
                n = generator.nextInt(n);
                fname = "PlantPic" + n + ".png";
                File file = new File(myDir, fname);
                Uri outputFileUri = Uri.fromFile(file);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(cameraIntent, 1313);
            }
        });

        prepareGallery();
        }

        private void prepareGallery() {


            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/Greenthumb";
           // photoDatabase = AppDatabase.getInstance(FetchPhoto.this);
          //  new RetrieveTask(this).execute();
            //todo get a list of photo paths from database
            //todo display that list

            File targetDirectory = new File(root);

            try {
                File[] files = targetDirectory.listFiles();
                for (File file : files) {
                    Uri uri = Uri.fromFile(file);
                    myRecyclerViewAdapter.add(myRecyclerViewAdapter.getItemCount(), uri);
                }
            }
                 catch (Exception e) {
                    e.printStackTrace();
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
            IMG.setVisibility(View.VISIBLE);

            //todo make options to delete a photo, and a X button to make IMG invisible

            Toast.makeText(FetchPhoto.this, stringitemUri, Toast.LENGTH_SHORT).show();
        }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) //operation was successful
        {

            if (requestCode == 1313) //then we're taking a new photo
            {


                /*
                //this code creates a lower quality but faster performing bitmap
                bitmap = (Bitmap) data.getExtras().get("data");
                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                File myDir = new File(root + "/Greenthumb");
                myDir.mkdirs();
                Random generator = new Random();
                int n = 1000000;
                n = generator.nextInt(n);
                fname = "PlantPic" + n + ".png";
                File file = new File(myDir, fname);



                if (file.exists())
                    file.delete();
                try {
                    //todo fix the quality of the photo
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG , 100, bytes);
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(bytes.toByteArray());
                    //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                // Tell the media scanner about the new file so that it is
                // immediately available to the user.
                MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });
                        */
                //todo delete the duplicate photo that is saved in the gallery.
                /*delete the duplicate photo*/
                /*add the photo to the database*/
                /*restart the activity with the new photo in the list*/

                finish();
                //Intent addPhotoIntent = new Intent(FetchPhoto.this, AddPhoto.class);
               // startActivity(addPhotoIntent);

                Intent intent = new Intent(FetchPhoto.this, AddPhoto.class);
                intent.putExtra("path", root + "/Greenthumb/" + fname);
                intent.putExtra("x", x);
                intent.putExtra("y", y);

                startActivity(intent);

            }
        }
     }
  }
