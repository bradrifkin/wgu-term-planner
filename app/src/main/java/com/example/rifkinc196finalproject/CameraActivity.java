package com.example.rifkinc196finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    private ImageView cameraFrame;
    private TextView cameraStatus;

    private boolean imageCaptured = false;
    protected boolean imageTaken;
    protected static final String PHOTO_TAKEN = "photo_taken";
    protected String folder;
    protected String imagePath;

    private Uri parentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        parentUri = getIntent().getParcelableExtra("PARENT_URI");
        folder = getExternalFilesDir(null)
                + "/class_tracker_images/";
        imagePath = folder + "tmpImage.jpg";

        takePicture();
    }


    protected void takePicture() {
        Log.i("CameraActivity", "Taking a picture");
        File _folder = new File(folder);
        _folder.mkdir();

        File file = new File(imagePath);
//        Uri outputFileUri = Uri.fromFile(file);
        Uri outputFileUri = FileProvider.getUriForFile(CameraActivity.this, BuildConfig.APPLICATION_ID + ".provider",file);

        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log message
        Log.i("CameraActivity", "resultCode: " + resultCode);
        switch (resultCode) {
            //When user doesn't capture image, resultcode returns 0
            case 0:
                Log.i("CameraActivity", "User cancelled");
                setResult(RESULT_CANCELED);
                finish();
                //When user captures image, onPhotoTaken an user-defined method
                //to assign the capture image to ImageView
            case -1:
                onPhotoTaken();
                break;
        }
    }

    protected void onPhotoTaken() {
        Log.i("CameraActivity", "Photo was Taken");
        imageTaken = true;
        imageCaptured = true;

        long now = DateUtil.todayLongWithTime();


        File from = new File(imagePath);
        File to = new File(folder + now + ".jpg");
        from.renameTo(to);

        Bitmap src = BitmapFactory.decodeFile(folder + now + ".jpg");

        if (src == null) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        DataManager.insertImage(this, now, parentUri);

        Bitmap thumb = ThumbnailUtils.extractThumbnail(src, (src.getWidth()/5), (src.getHeight()/5) );
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(folder + now + "_thumb.png");
            thumb.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        setResult(RESULT_OK);
        finish();
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(CameraActivity.PHOTO_TAKEN, imageTaken);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.getBoolean(CameraActivity.PHOTO_TAKEN)) {
            onPhotoTaken();
        }
    }

}
