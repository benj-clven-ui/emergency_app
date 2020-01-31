package com.myapp.emergency;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FriendContact extends AppCompatActivity {

    DB_Friend_Helper mydbf;
    private Intent i = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.layout_addfriend );
        Button back = (Button) findViewById( R.id.back );
        Button add = (Button) findViewById( R.id.addbtn );
        mydbf = new DB_Friend_Helper( this );


        ArrayList list = mydbf.getAllContacts();

        MyAdapterFriend adapter = new MyAdapterFriend( list, this );
        ListView lView = (ListView) findViewById( R.id.listView12 );
        lView.setAdapter( adapter );


        back.setOnClickListener( new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                i.setClass( getApplicationContext(), Contacts.class );
                startActivity( i );


            }
        } );

        add.setOnClickListener( new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                Bundle dataBundle = new Bundle();
                dataBundle.putInt( "id", 0 );

                Intent intent = new Intent( getApplicationContext(), FriendAdd.class );
                intent.putExtras( dataBundle );

                startActivity( intent );
                return;
            }
        } );


    }
}

