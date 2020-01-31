package com.myapp.emergency;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;

public class ProfileSignUp extends AppCompatActivity {

    private Button btnChoose, btnCam;
    private CircularImageView imageView;

    private StorageReference Folder;

    private FirebaseAuth fAuth;
    ProgressBar progressBar;
    DatabaseReference databaseReference;

    private final int PICK_IMAGE_REQUEST = 1;
    private final int CAMERA_REQUEST_CODE = 2;

    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.signup_prof );

        btnCam = (Button)findViewById ( R.id.btnCam );
        btnChoose = (Button) findViewById ( R.id.btnChoose );
        imageView = (CircularImageView) findViewById ( R.id.imgView );
        Folder = FirebaseStorage.getInstance ().getReference ().child ( "ImageFolder" );

        databaseReference = FirebaseDatabase.getInstance ().getReference ( "Users" );
        fAuth = FirebaseAuth.getInstance ();

        progressBar = findViewById ( R.id.progressbar );

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( getApplicationContext (), MainActivity.class ) );            }
        });

        btnCam.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }
            }
        } );

    }


    public void UploadData(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK
                && data != null && data.getData () != null) {

            Uri uri = data.getData ();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap ( getContentResolver (), uri );
                imageView.setImageBitmap ( bitmap );
            } catch (IOException e) {
                e.printStackTrace ();
            }

        }

//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
//                && data != null && data.getData () != null) {
//            filePath = data.getData ();
//
//            StorageReference ImagenName = Folder.child ( "image" + filePath.getLastPathSegment () );
//            ImagenName.putFile ( filePath ).addOnSuccessListener ( new OnSuccessListener<UploadTask.TaskSnapshot> () {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText ( ProfileSignUp.this, "Profile Uploaded", Toast.LENGTH_SHORT ).show ();
//                }
//            } );
//
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap ( getContentResolver (), filePath );
//                imageView.setImageBitmap ( bitmap );
//            } catch (IOException e) {
//                e.printStackTrace ();
//            }
//        }

        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri ImageData = data.getData ();

                final StorageReference ImagenName = Folder.child ( "image" + ImageData.getLastPathSegment () );

                ImagenName.putFile ( ImageData ).addOnSuccessListener ( new OnSuccessListener<UploadTask.TaskSnapshot> () {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText ( ProfileSignUp.this, "Profile Uploaded", Toast.LENGTH_LONG ).show ();

                        ImagenName.getDownloadUrl ().addOnSuccessListener ( new OnSuccessListener<Uri> () {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference imagestore = FirebaseDatabase.getInstance ().getReference ("Users").child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ());

//                                HashMap<String,String>hashMap = new HashMap<> ( );
//                                hashMap.put ( "imageurl", String.valueOf ( uri ) );
                                DatabaseReference con = imagestore.child ( "imageurl" );
                                con.setValue ( String.valueOf ( uri ) );
                                Toast.makeText ( ProfileSignUp.this, "Profile Uploaded in Users", Toast.LENGTH_LONG ).show ();

//                                imagestore.setValue ( hashMap ).addOnSuccessListener ( new OnSuccessListener<Void> () {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Toast.makeText ( ProfileSignUp.this, "Profile Uploaded in Users", Toast.LENGTH_LONG ).show ();
//                                    }
//                                } );
                            }
                        } );
                    }
                } );

            }
        }
    }


}




























//        btnChoose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chooseImage();
//            }
//        });
//    }
//    private void chooseImage() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
////        if (requestCode == PICK_IMAGE_REQUEST) {
////            if (resultCode == RESULT_OK){
////                Uri ImageData = data.getData ();
////
////                DatabaseReference Imagename = databaseReference.child ( "image"+ImageData.getLastPathSegment () );
////
////
////
////            }
////        }
//
//        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
//                && data != null && data.getData() != null ) {
//            filePath = data.getData ();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap ( getContentResolver (), filePath );
//                imageView.setImageBitmap ( bitmap );
//            } catch (IOException e) {
//                e.printStackTrace ();
//            }
//        }
