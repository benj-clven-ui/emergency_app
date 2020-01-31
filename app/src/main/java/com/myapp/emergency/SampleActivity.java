package com.myapp.emergency;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SampleActivity extends AppCompatActivity {

    int  MY_PERMISSION_REQUEST_SEND_SMS;
    FirebaseDatabase db;
    DatabaseReference reff;
    Notification notifi;
    long maxid=0;
//    EditText notif;
//    Button btnsave;
    GPSTracker gps;
    private static final int REQUEST_CODE_PERMISSION = 3;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.samplelayout );

        db= FirebaseDatabase.getInstance ();

//        notif =(EditText)findViewById ( R.id.notif );
//        btnsave =(Button)findViewById ( R.id.btnsave );
        notifi = new Notification ();


        reff = db.getReference ("Users").child (FirebaseAuth.getInstance ().getCurrentUser ().getUid ()).child ( "Notif" );
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

            }
            public void logout (View view){
                FirebaseAuth.getInstance ().signOut();
                startActivity ( new Intent ( getApplicationContext (), Login.class ) );
                finish ();
            }

    public void mess1(View view) {

        gps = new GPSTracker(SampleActivity.this);
        if(gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            String uri = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
//            Toast.makeText(getApplicationContext(),uri,Toast.LENGTH_SHORT).show();

            StringBuffer message = new StringBuffer();
            message.append( Uri.parse( uri ) );
            String msg = "Emergency!! im in danger.  I need Your help Immediately. This is my Location! ";
            String phon = "09352984656";
            //            09352984656  evngelista
//            09564674430 taba
            notifi.setNotif ( msg.toString().trim () );
            notifi.setLink ( message.toString().trim () );

            reff.child ( String.valueOf ( maxid+1 ) ).setValue (notifi);

            if (ContextCompat.checkSelfPermission( this, Manifest.permission.SEND_SMS )
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSION_REQUEST_SEND_SMS );
            } else {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage( phon, null, msg+message.toString(), null, null );

            }
        }

    }

}









//
//
//
//package com.myapp.emergency;
//
//        import android.Manifest;
//        import android.app.Activity;
//        import android.app.AlertDialog;
//        import android.content.Context;
//        import android.content.DialogInterface;
//        import android.content.Intent;
//        import android.content.SharedPreferences;
//        import android.content.pm.PackageManager;
//        import android.net.Uri;
//        import android.os.Bundle;
//        import android.provider.Settings;
//
//        import androidx.appcompat.app.AppCompatActivity;
//        import androidx.core.app.ActivityCompat;
//        import androidx.core.content.ContextCompat;
//
//public class SampleActivity extends AppCompatActivity {
//    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
//    public static final String ALLOW_KEY = "ALLOWED";
//    public static final String CAMERA_PREF = "camera_pref";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            if (getFromPref(this, ALLOW_KEY)) {
//                showSettingsAlert();
//            } else if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.CAMERA)
//
//                    != PackageManager.PERMISSION_GRANTED) {
//
//                // Should we show an explanation?
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.CAMERA)) {
//                    showAlert();
//                } else {
//                    // No explanation needed, we can request the permission.
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.CAMERA},
//                            MY_PERMISSIONS_REQUEST_CAMERA);
//                }
//            }
//        } else {
//            openCamera();
//        }
//
//    }
//    public static void saveToPreferences(Context context, String key, Boolean allowed) {
//        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor prefsEditor = myPrefs.edit();
//        prefsEditor.putBoolean(key, allowed);
//        prefsEditor.commit();
//    }
//
//    public static Boolean getFromPref(Context context, String key) {
//        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
//                Context.MODE_PRIVATE);
//        return (myPrefs.getBoolean(key, false));
//    }
//
//    private void showAlert() {
//        AlertDialog alertDialog = new AlertDialog.Builder(SampleActivity.this).create();
//        alertDialog.setTitle("Alert");
//        alertDialog.setMessage("App needs to access the Camera.");
//
//        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        finish();
//                    }
//                });
//
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
//                new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        ActivityCompat.requestPermissions(SampleActivity.this,
//                                new String[]{Manifest.permission.CAMERA},
//                                MY_PERMISSIONS_REQUEST_CAMERA);
//                    }
//                });
//        alertDialog.show();
//    }
//
//    private void showSettingsAlert() {
//        AlertDialog alertDialog = new AlertDialog.Builder(SampleActivity.this).create();
//        alertDialog.setTitle("Alert");
//        alertDialog.setMessage("App needs to access the Camera.");
//
//        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
//                new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        //finish();
//                    }
//                });
//
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
//                new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        startInstalledAppDetailsActivity(SampleActivity.this);
//                    }
//                });
//
//        alertDialog.show();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_CAMERA: {
//                for (int i = 0, len = permissions.length; i < len; i++) {
//                    String permission = permissions[i];
//
//                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                        boolean
//                                showRationale =
//                                ActivityCompat.shouldShowRequestPermissionRationale(
//                                        this, permission);
//
//                        if (showRationale) {
//                            showAlert();
//                        } else if (!showRationale) {
//                            // user denied flagging NEVER ASK AGAIN
//                            // you can either enable some fall back,
//                            // disable features of your app
//                            // or open another dialog explaining
//                            // again the permission and directing to
//                            // the app setting
//                            saveToPreferences(SampleActivity.this, ALLOW_KEY, true);
//                        }
//                    }
//                }
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    public static void startInstalledAppDetailsActivity(final Activity context) {
//        if (context == null) {
//            return;
//        }
//
//        final Intent i = new Intent();
//        i.setAction( Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        i.addCategory(Intent.CATEGORY_DEFAULT);
//        i.setData(Uri.parse("package:" + context.getPackageName()));
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//        context.startActivity(i);
//    }
//
//    private void openCamera() {
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        startActivity(intent);
//    }
//}
