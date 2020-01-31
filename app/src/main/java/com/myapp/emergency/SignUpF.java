package com.myapp.emergency;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpF extends AppCompatActivity {

    EditText mEmail,mAddress,mPassword,mFullname,mPhon,mBday,mAge;
    Button mRegister;
    CheckBox show_hide_password;
    RadioButton GenderMale, GenderFemale;
    String gender="";
    TextView mForgotPass;



    private FirebaseAuth fAuth;
    ProgressBar progressBar;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_final);

        mFullname = findViewById ( R.id.Fullname );
        mBday = findViewById ( R.id.Bday );
        mAge = findViewById ( R.id.Age );
        mAddress = findViewById ( R.id.Address );
        mPhon = findViewById ( R.id.Phon );
        mEmail = findViewById ( R.id.Email );
        mPassword = findViewById ( R.id.Password );
        mRegister = findViewById ( R.id.Register );
        mForgotPass = findViewById ( R.id.forgot_password );
        show_hide_password = findViewById ( R.id.show_hide_password );
        GenderMale = findViewById ( R.id.male );
        GenderFemale = findViewById ( R.id.female );

//        btnChoose = (Button) findViewById(R.id.btnChoose);
//        imageView = (CircularImageView) findViewById(R.id.imgView);

        databaseReference = FirebaseDatabase.getInstance ().getReference ("Users");
        fAuth = FirebaseAuth.getInstance ();

        progressBar = findViewById ( R.id.progressbar );


        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener () {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            mPassword.setInputType( InputType.TYPE_CLASS_TEXT);
                            mPassword.setTransformationMethod( HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            mPassword.setInputType( InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            mPassword.setTransformationMethod( PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });


        mForgotPass.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( getApplicationContext (), ForgotPass.class ) );
            }
        } );

        mRegister.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                final String fullname = mFullname.getText ().toString ().trim ();
                final String birthday = mBday.getText ().toString ().trim ();
                final String age = mAge.getText ().toString ().trim ();
                final String address = mAddress.getText ().toString ().trim ();
                final String number = mPhon.getText ().toString ().trim ();
                final String email = mEmail.getText ().toString ().trim ();
                String password = mPassword.getText ().toString ().trim ();

                if (GenderMale.isChecked ()){
                    gender = "Male";
                }
                if (GenderFemale.isChecked ()){
                    gender = "Female";
                }

                if (number.length () < 11) {
                    mPhon.setError ( "11 digit Cp Number is Required" );
                    return;
                }
                if (TextUtils.isEmpty ( fullname )) {
                    mFullname.setError ( "Full Name is Required" );
                    return;
                }
                if (TextUtils.isEmpty ( birthday )) {
                    mFullname.setError ( "BirthDay is Required" );
                    return;
                }
                if (TextUtils.isEmpty ( age )) {
                    mFullname.setError ( "Age is Required" );
                    return;
                }
                if (TextUtils.isEmpty ( address )) {
                    mAddress.setError ( "Address is Required" );
                    return;
                }
                if (TextUtils.isEmpty ( email )) {
                    mEmail.setError ( "Email is Required" );
                    return;
                }
                if (TextUtils.isEmpty ( password )) {
                    mPassword.setError ( "Password is Required" );
                    return;
                }
                if (password.length () < 4){
                    mPassword.setError ( "Password must have 4 Characters" );
                    return;
                }

                progressBar.setVisibility ( View.VISIBLE );

                    fAuth.signInWithEmailAndPassword ( email,password ).addOnCompleteListener ( new OnCompleteListener<AuthResult> () {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility ( View.GONE );
                        if (task.isSuccessful ()) {
                            User information = new User (
                                    fullname,
                                    birthday,
                                    age,
                                    address,
                                    email,
                                    number,
                                    gender
                            );

                            FirebaseDatabase.getInstance ().getReference ("Users")
                                    .child ( FirebaseAuth.getInstance ().getCurrentUser ().getUid () )
                                    .setValue ( information ).addOnCompleteListener ( new OnCompleteListener<Void> () {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful ()){
                                        Toast.makeText ( SignUpF.this, "User Register Successfully", Toast.LENGTH_SHORT ).show ();
                                    }else {
                                        Toast.makeText ( SignUpF.this, "User Unsuccessfully Register", Toast.LENGTH_SHORT ).show ();
                                    }
                                }
                            } );

                            Toast.makeText ( SignUpF.this, "Please set your profile Picture befod", Toast.LENGTH_SHORT ).show ();
                            startActivity ( new Intent ( getApplicationContext (), ProfileSignUp.class ) );
                        }else {
                            Toast.makeText ( SignUpF .this, "Error !!" + task.getException ().getMessage (), Toast.LENGTH_SHORT ).show ();
                        }
                    }
                } );
            }
        } );

        TextView signIn_text = findViewById(R.id.signIn_text);
        signIn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpF.this, Login.class));
                finish();
            }
        });
    }

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
//    }
}