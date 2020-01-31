package com.myapp.emergency;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class FamilyContacts extends AppCompatActivity {

    DB_Family_Helper mydb;
    private Intent i = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.layout_addfam );

        mydb = new DB_Family_Helper( this );
        ArrayList list = mydb.getAllContacts();

        Button back = (Button) findViewById( R.id.back );
        Button add = (Button) findViewById( R.id.addbtn );

        MyAdapterFam adapter = new MyAdapterFam( list, getApplicationContext() );
        ListView lView = (ListView)findViewById(R.id.listView1);


        lView.setAdapter(adapter);

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

                Intent intent = new Intent( getApplicationContext(), FamilyAdd.class );
                intent.putExtras( dataBundle );

                startActivity( intent );
                return;
            }
        } );
    }
}