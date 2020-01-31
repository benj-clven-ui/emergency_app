package com.myapp.emergency;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FamilyAdd extends AppCompatActivity {

    private DB_Family_Helper mydbf;

    TextView name;
    TextView phone;
    TextView address;
    private Intent i = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.my_list_add_family );
        Button back = (Button) findViewById( R.id.back );

        name = (TextView) findViewById( R.id.editTextNameFriend );
        phone = (TextView) findViewById( R.id.editTextPhoneFriend );
        address = (TextView) findViewById( R.id.editTextAddressFriend );

        mydbf = new DB_Family_Helper( this );


            back.setOnClickListener( new View.OnClickListener() {

                @Override

                public void onClick(View v) {
                    i.setClass( getApplicationContext(), FamilyContacts.class );
                    startActivity( i );


                }
            } );
    }

    public void addon(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (mydbf.insertContact( name.getText().toString(), phone.getText().toString(),
                    address.getText().toString() )) {
                Toast.makeText( getApplicationContext(), "done",
                        Toast.LENGTH_SHORT ).show();
            } else {
                Toast.makeText( getApplicationContext(), "not done",
                        Toast.LENGTH_SHORT ).show();
            }
            Intent intent = new Intent( getApplicationContext(), FamilyContacts.class );
            startActivity( intent );
        }
    }
}