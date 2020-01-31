package com.myapp.emergency;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPass extends AppCompatActivity {

    ProgressBar progressBar;
    EditText mEmail;
    Button mRetrieve;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView(R.layout.forgot_pass);

        progressBar = findViewById ( R.id.progressbar );
        mEmail = findViewById ( R.id.Email );
        mRetrieve = findViewById ( R.id.Retrieve );

        firebaseAuth = FirebaseAuth.getInstance ();


        mRetrieve.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility ( View.VISIBLE );
                firebaseAuth.sendPasswordResetEmail ( mEmail.getText ().toString()).addOnCompleteListener ( new OnCompleteListener<Void> () {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility ( View.GONE );

                        if (task.isSuccessful ()){
                            Toast.makeText ( ForgotPass.this,
                                    "Password Send, Check Your Email !", Toast.LENGTH_SHORT ).show ();
                        }else {
                            Toast.makeText ( ForgotPass.this,
                                    task.getException ().getMessage (), Toast.LENGTH_SHORT ).show ();

                        }

                    }
                } );

            }
        } );

        TextView signUp_text = findViewById(R.id.signIn_text);
        signUp_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPass.this, Login.class));
                finish();
            }
        });
    }
}
