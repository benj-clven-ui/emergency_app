package com.myapp.emergency;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;


public class FriendAdd extends AppCompatActivity {

    private DB_Friend_Helper mydbf;
    TextView name;
    TextView phone;
    TextView address;
    int id_To_Update = 0;
    private Intent i = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.my_list_add_friend );
        Button back = (Button) findViewById( R.id.back );

        name = (TextView) findViewById( R.id.editTextNameFriend );
        phone = (TextView) findViewById( R.id.editTextPhoneFriend );
        address = (TextView) findViewById( R.id.editTextAddressFriend );

        mydbf = new DB_Friend_Helper( this );


        back.setOnClickListener( new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                i.setClass( getApplicationContext(), FriendContact.class );
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
            Intent intent = new Intent( getApplicationContext(), FriendContact .class );
            startActivity( intent );
        }
    }
}