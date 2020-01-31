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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity {

    EditText mEmail,mPassword;
    TextView mForgotPass;
    Button mRegister;
    CheckBox show_hide_password;

    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mEmail = findViewById ( R.id.Email );
        mPassword = findViewById ( R.id.mPass );
        mRegister = findViewById ( R.id.mLogin );
        mForgotPass = findViewById ( R.id.forgot_password );

        show_hide_password = findViewById ( R.id.show_hide_password );

        fAuth = FirebaseAuth.getInstance ();

        progressBar = findViewById ( R.id.progressbar );



        if (fAuth.getCurrentUser () != null){
            startActivity ( new Intent ( getApplicationContext (), MainActivity.class ) );
            finish ();
        }

        mForgotPass.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( getApplicationContext (), ForgotPass.class ) );
            }
        } );


        show_hide_password
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener () {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            mPassword.setInputType(InputType.TYPE_CLASS_TEXT);
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

        mRegister.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText ().toString ().trim ();
                String password = mPassword.getText ().toString ().trim ();

                if (TextUtils.isEmpty ( email )) {
                    mEmail.setError ( "Email is Required" );
                    return;
                }
                if (TextUtils.isEmpty ( password )) {
                    mPassword.setError ( "Password is Required" );
                    return;
                }
                if (password.length () < 4){
                    mPassword.setError ( "Password must be >= 4 Characters" );
                    return;
                }

                progressBar.setVisibility ( View.VISIBLE );

                fAuth.signInWithEmailAndPassword ( email,password ).addOnCompleteListener ( new OnCompleteListener<AuthResult> () {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful ()) {
                            Toast.makeText ( Login.this, "Successfully Logged in", Toast.LENGTH_SHORT ).show ();
                            startActivity ( new Intent ( getApplicationContext (), MainActivity.class ) );
                        }else {
                            Toast.makeText ( Login .this, "Error !!" + task.getException ().getMessage (), Toast.LENGTH_SHORT ).show ();
                        }
                    }
                } );
            }
        } );


        TextView signUp_text = findViewById(R.id.signUp_text);
        signUp_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, TakePic.class));
                finish();
            }
        });
    }
}





