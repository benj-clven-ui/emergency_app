package com.myapp.emergency;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePic extends AppCompatActivity {


    private Button btnChoose, btnCam;
    private CircularImageView imageView;

    private StorageReference Folder;

    private FirebaseAuth fAuth;
    ProgressBar progressBar;
    DatabaseReference databaseReference;

    private final int CAMERA_REQUEST_CODE = 5;
    private final int GALLERY_REQUEST_CODE = 5;
    String currentPhotoPath;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.take_pic );

        btnCam = (Button) findViewById ( R.id.btnCam );
        btnChoose = (Button) findViewById ( R.id.btnChoose );
        imageView = (CircularImageView) findViewById ( R.id.imgView );

        Folder = FirebaseStorage.getInstance ().getReference ().child ( "ImageFolder" );

//        databaseReference = FirebaseDatabase.getInstance ().getReference ( "Users" );
//        fAuth = FirebaseAuth.getInstance ();

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

        btnCam.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
//                Toast.makeText ( TakePic.this, "Camera Button", Toast.LENGTH_SHORT ).show ();

                dispatchTakePictureIntent();


            }
        } );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (requestCode == RESULT_OK){

                File pic = new File ( currentPhotoPath );
                Uri contentUri = Uri.fromFile(pic);
//                imageView.setImageURI (contentUri);

                Toast.makeText ( TakePic.this, "Camera Image:  " + contentUri, Toast.LENGTH_SHORT ).show ();

            }

//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);

        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(TakePic.this, "com.myapp.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat ("yyyyMMdd_HHmmss").format(new Date ());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir( Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
