package com.myapp.emergency;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    private Intent i = new Intent();
    int  MY_PERMISSION_REQUEST_SEND_SMS;

    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";

    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver;

    private StorageReference Folder;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CODE_PERMISSION = 3;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    GPSTracker gps;

    FirebaseDatabase db;

    Notification notifi;
    ImageNotif imgNotif;
    CallHistory callnotifi;


    DatabaseReference reff;
    DatabaseReference callreff;
    DatabaseReference imagereff;

    long maxid=0;
    long newid=0;
    long myid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        Button img = (Button) findViewById ( R.id.adding );
        sentPI = PendingIntent.getBroadcast ( this, 0, new Intent ( SENT ), 0 );
        deliveredPI = PendingIntent.getBroadcast ( this, 0, new Intent ( DELIVERED ), 0 );

        Folder = FirebaseStorage.getInstance ().getReference ().child ( "NewImage" );


        db= FirebaseDatabase.getInstance ();
        manageConnections();
        notifi = new Notification ();
        callnotifi = new CallHistory ();
        imgNotif = new ImageNotif ();



        reff = db.getReference ("Users").child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ()).child ( "Notif" );
        callreff = db.getReference ("Users").child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ()).child ( "CallNotif" );
        imagereff = db.getReference ("Users").child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ()).child ( "Images" );

        reff.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists ()){
                    maxid=(dataSnapshot.getChildrenCount ());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        callreff.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists ()){
                    myid=(dataSnapshot.getChildrenCount ());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        imagereff.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists ()){
                    newid=(dataSnapshot.getChildrenCount ());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


        try {
            if (ActivityCompat.checkSelfPermission ( this, mPermission )
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions ( this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION );

                // If any permission above not allowed by user, this condition will
//                execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }

        img.setOnClickListener ( new View.OnClickListener () {

            @Override

            public void onClick(View v) {
                i.setClass ( getApplication (), Contacts.class );
                startActivity ( i );

                }
            });
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
            if (requestCode == PICK_IMAGE_REQUEST) {
                if (resultCode == RESULT_OK) {
                    Uri ImageData = data.getData ();

                    final StorageReference ImagenName = Folder.child ( ImageData.getLastPathSegment () );

                    ImagenName.putFile ( ImageData ).addOnSuccessListener ( new OnSuccessListener<UploadTask.TaskSnapshot> () {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ImagenName.getDownloadUrl ().addOnSuccessListener ( new OnSuccessListener<Uri> () {
                                @Override
                                public void onSuccess(Uri uri) {
//
                                    imgNotif.setPics ( uri.toString().trim () );
                                    imagereff.child ( String.valueOf ( newid + 1 ) ).setValue ( imgNotif );
//                                    imagereff.child ( setValue (imgNotif) );

                                    Toast.makeText ( MainActivity.this, "Image sent", Toast.LENGTH_LONG ).show ();

                                }
                            } );
                        }
                    } );

                }
            }
        }

    private void manageConnections() {
            final DatabaseReference infoConnected = db.getReference (".info/connected");
            final DatabaseReference connectionReference = db.getReference ("Users").child ( FirebaseAuth.getInstance ().getCurrentUser ().getUid ());

            infoConnected.addValueEventListener ( new ValueEventListener () {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean connected = dataSnapshot.getValue (Boolean.class);

                        if (connected){
                            DatabaseReference con = connectionReference.child ( "Status" );
                            con.setValue ( "Online" );
                            con.onDisconnect ().setValue ( "Offline" );
//                            ServerValue.TIMESTAMP
//                            DatabaseReference time = connectionReference.child ( "LastConnected" );
//                            time.onDisconnect ().setValue ( ServerValue.TIMESTAMP );

                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    @Override
    protected void onResume() {
        super.onResume();

        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText( MainActivity.this, "SMS sent!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText( MainActivity.this, "SMS not sent!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText( MainActivity.this, "No Service!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        registerReceiver( smsSentReceiver, new IntentFilter( SENT ) );
    }


    public void alarmBut(View view) {

        final MediaPlayer mp = MediaPlayer.create( this, R.raw.alarm );

        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);

        mp.start();


//          FOR 911 ...................................
        gps = new GPSTracker(MainActivity.this);
        if(gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            String uri = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
            StringBuffer message = new StringBuffer();
            message.append( Uri.parse( uri ) );
            String msg = "911 Rescuer! This Account press the ALARM. This is his/her Location! ";
            String phon = "09652268926";  //            09652268926 benj
            String phon2 = "09566305892";  //            Tiana

            if (ContextCompat.checkSelfPermission( this, Manifest.permission.SEND_SMS )
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSION_REQUEST_SEND_SMS );
            } else {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage( phon, null, msg+message.toString(), sentPI, deliveredPI );
                sms.sendTextMessage( phon2, null, msg+message.toString(), sentPI, deliveredPI );

            }
        }

    //        FOR FIRE   .............................
        gps = new GPSTracker(MainActivity.this);
        if(gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            String uri = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
            StringBuffer message = new StringBuffer();
            message.append( Uri.parse( uri ) );
            String msg = "FIRE Rescuer! This Account press the ALARM. This is his/her Location! ";
            String phon = "09771007998";  //            09771007998  merl
            String phon2 = "09165282319";  //            09361234412  daboy

            if (ContextCompat.checkSelfPermission( this, Manifest.permission.SEND_SMS )
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSION_REQUEST_SEND_SMS );
            } else {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage( phon, null, msg+message, sentPI, deliveredPI );
                sms.sendTextMessage( phon2, null, msg+message, sentPI, deliveredPI );
            }
        }

//              FOR POLICE .........................
        gps = new GPSTracker(MainActivity.this);
        if(gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            String uri = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
            StringBuffer message = new StringBuffer();
            message.append( Uri.parse( uri ) );
            String msg = "This Account press the ALARM. This is his/her Location! ";
            String phon = "09064954672";  //             09064954672 lamor
            String phon2 = "09458378958";  //            09458378958 joshua

            if (ContextCompat.checkSelfPermission( this, Manifest.permission.SEND_SMS )
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSION_REQUEST_SEND_SMS );
            } else {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage( phon, null, msg + message, sentPI, deliveredPI );
                sms.sendTextMessage( phon2, null, msg + message, sentPI, deliveredPI );
            }
        }
    }


    //          FOR 911 ...................................
    public void mess1(View view) {

        gps = new GPSTracker(MainActivity.this);
        if(gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            String uri = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
            StringBuffer message = new StringBuffer();
            message.append( Uri.parse( uri ) );
            String msg = "Emergency!! im in danger.  I need Your help Immediately. This is my Location! ";
            String phon = "09652268926";  //            09652268926 benj
            String phon2 = "09566305892";  //            Tiana


            notifi.setNotif ( msg.toString().trim () );
            notifi.setLink ( message.toString().trim () );
            reff.child ( String.valueOf ( maxid+1 ) ).setValue (notifi);

            if (ContextCompat.checkSelfPermission( this, Manifest.permission.SEND_SMS )
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSION_REQUEST_SEND_SMS );
            } else {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage( phon, null, msg+message.toString(), sentPI, deliveredPI );
                sms.sendTextMessage( phon2, null, msg+message.toString(), sentPI, deliveredPI );

            }
        }

    }

    //        FOR FIRE   .............................
    public void mess2(View view) {

        gps = new GPSTracker(MainActivity.this);
        if(gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            String uri = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
            StringBuffer message = new StringBuffer();
            message.append( Uri.parse( uri ) );
            String msg = "Help! There is Fire in my Area. This is my Location! ";
            String phon = "09771007998";  //            09771007998  merl
            String phon2 = "09165282319";  //            09361234412  daboy

            notifi.setNotif ( msg.toString().trim () );
            notifi.setLink ( message.toString().trim () );
            reff.child ( String.valueOf ( maxid+1 ) ).setValue (notifi);

            if (ContextCompat.checkSelfPermission( this, Manifest.permission.SEND_SMS )
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSION_REQUEST_SEND_SMS );
            } else {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage( phon, null, msg+message, sentPI, deliveredPI );
                sms.sendTextMessage( phon2, null, msg+message, sentPI, deliveredPI );
            }
        }
    }

    //          FOR POLICE ...................................
    public void mess3(View view) {

        gps = new GPSTracker(MainActivity.this);
        if(gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            String uri = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
            StringBuffer message = new StringBuffer();
            message.append( Uri.parse( uri ) );
            String msg = "Police Officer!! im in danger.  I need You Immediately. This is my Location! ";
            String phon = "09064954672";  //             09064954672 lamor
            String phon2 = "09458378958";  //            09458378958 joshua

            notifi.setNotif ( msg.toString().trim () );
            notifi.setLink ( message.toString().trim () );
            reff.child ( String.valueOf ( maxid+1 ) ).setValue (notifi);

            if (ContextCompat.checkSelfPermission( this, Manifest.permission.SEND_SMS )
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSION_REQUEST_SEND_SMS );
            } else {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage( phon, null, msg + message, sentPI, deliveredPI );
                sms.sendTextMessage( phon2, null, msg + message, sentPI, deliveredPI );
            }
        }
    }

    //          FOR 911 ...................................
    public void emergency1(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:09652268926"));  //            09564674430  taba


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    10);
            return;
        }else {
            try {
                startActivity ( callIntent );
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText ( getApplicationContext (), "yourActivity is not founded", Toast.LENGTH_SHORT ).show ();
            }
        }

        gps = new GPSTracker(MainActivity.this);
        if(gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            String uri = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
            StringBuffer message = new StringBuffer();
            message.append( Uri.parse( uri ) );
            String msg = "Emergency!! im in danger.  I need Your help Immediately. This is my Location! ";
            String phon = "09652268926";  //            09652268926 benj
            String phon2 = "09566305892";  //            Tiana

            String messagef = "This Account Call The 911 Station Recently. " + message;
            callnotifi.setCall ( messagef.toString().trim () );
            callreff.child ( String.valueOf ( myid+1 ) ).setValue (callnotifi);


            if (ContextCompat.checkSelfPermission( this, Manifest.permission.SEND_SMS )
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSION_REQUEST_SEND_SMS );
            } else {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage( phon, null, msg+message.toString(), sentPI, deliveredPI );
                sms.sendTextMessage( phon2, null, msg+message.toString(), sentPI, deliveredPI );
            }
        }
    }

    //        FOR FIRE   .............................
    public void emergency2(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:09771007998"));  //        09771007998  merl

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

        gps = new GPSTracker(MainActivity.this);
        if(gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            String uri = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
            StringBuffer message = new StringBuffer();
            message.append( Uri.parse( uri ) );
            String msg = "Help! There is Fire in my Area. This is my Location! ";
            String phon = "09771007998";  //            09771007998  merl
            String phon2 = "09165282319";  //            09165282319  abdul

            String messagef = "This Account Call The Fire Station Recently. " + message;
            callnotifi.setCall ( messagef.toString().trim () );
            callreff.child ( String.valueOf ( myid+1 ) ).setValue (callnotifi);


            if (ContextCompat.checkSelfPermission( this, Manifest.permission.SEND_SMS )
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSION_REQUEST_SEND_SMS );
            } else {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage( phon, null, msg+message, sentPI, deliveredPI );
                sms.sendTextMessage( phon2, null, msg+message, sentPI, deliveredPI );
            }
        }

    }

    //        FOR POLICE   .............................
    public void emergency3(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:09064954672"));  //         09064954672 lamor


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
        gps = new GPSTracker(MainActivity.this);
        if(gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            String uri = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;


            StringBuffer message = new StringBuffer();
            message.append( Uri.parse( uri ) );
            String msg = "Police Officer!! im in danger.  I need You Immediately. This is my Location! ";
            String phon = "09064954672";  //             09064954672 lamor
            String phon2 = "09458378958";  //            09458378958 joshua

            String messagef = "This Account Call The Police Station Recently. " + message;
            callnotifi.setCall ( messagef.toString().trim () );
            callreff.child ( String.valueOf ( myid+1 ) ).setValue (callnotifi);

            if (ContextCompat.checkSelfPermission( this, Manifest.permission.SEND_SMS )
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSION_REQUEST_SEND_SMS );
            } else {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage( phon, null, msg + message, sentPI, deliveredPI );
                sms.sendTextMessage( phon2, null, msg + message, sentPI, deliveredPI );
            }
        }
    }

//  Optional Logout .......

//    public void logout(View view) {
//        FirebaseAuth.getInstance ().signOut();
//        startActivity ( new Intent ( getApplicationContext (), Login.class ) );
//        finish ();
//    }
}