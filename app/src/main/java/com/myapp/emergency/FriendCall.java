package com.myapp.emergency;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class FriendCall extends AppCompatActivity {

    private DB_Friend_Helper mydbf;

    TextView name;
    TextView phone;
    TextView address;
    private Intent i = new Intent();
    private Object view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.my_friend_call );
        Button back = (Button) findViewById( R.id.back );

        name = (TextView) findViewById( R.id.frName );
        address = (TextView) findViewById( R.id.frAddr );
        phone = (TextView) findViewById( R.id.frPhone );

        mydbf = new DB_Friend_Helper( this );

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt( "id" );

            if (Value > 0) {
                //means this is the view part not the add contact part.
                Cursor rs = mydbf.getData( Value );
                rs.moveToFirst();

                String nam = rs.getString( rs.getColumnIndex( DB_Friend_Helper.CONTACTS_COLUMN_NAME ) );
                String phon = rs.getString( rs.getColumnIndex( DB_Friend_Helper.CONTACTS_COLUMN_PHONE ) );
                String addre = rs.getString( rs.getColumnIndex( DB_Friend_Helper.CONTACTS_COLUMN_CITY ) );

                if (rs.isClosed()) {
                    rs.close();
                }

                name.setText( (CharSequence) nam );
                name.setFocusable( false );
                name.setClickable( false );

                phone.setText( (CharSequence) phon );
                phone.setFocusable( false );
                phone.setClickable( false );

                address.setText( (CharSequence) addre );
                address.setFocusable( false );
                address.setClickable( false );
            }

            back.setOnClickListener( new View.OnClickListener() {

                @Override

                public void onClick(View v) {
                    i.setClass( getApplicationContext(), FriendContact.class );
                    startActivity( i );


                }
            } );
        }
    }
    public void callFr(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        String number=phone.getText().toString();
        callIntent.setData(Uri.parse("tel: " + number));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    10);
            return;
        }else {
            try{
                startActivity(callIntent);
            }
            catch (android.content.ActivityNotFoundException ex){
                Toast.makeText(getApplicationContext(),"yourActivity is not founded",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
