package com.myapp.emergency;

import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class FriendEdit extends AppCompatActivity
{
    private DB_Friend_Helper mydbf ;

    TextView name ;
    TextView phone;
    TextView address;
    int id_To_Update = 0;
    private Intent i = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_list_view_friend);
        Button back = (Button) findViewById(R.id.back);

        name = (TextView) findViewById(R.id.editTextNameFriend);
        phone = (TextView) findViewById(R.id.editTextPhoneFriend);
        address = (TextView) findViewById(R.id.editTextAddressFriend);

        mydbf = new DB_Friend_Helper(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");

            if(Value>0){
                //means this is the view part not the add contact part.
                Cursor rs = mydbf.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();

                String nam = rs.getString(rs.getColumnIndex(DB_Friend_Helper.CONTACTS_COLUMN_NAME));
                String phon = rs.getString(rs.getColumnIndex(DB_Friend_Helper.CONTACTS_COLUMN_PHONE));
                String addre = rs.getString(rs.getColumnIndex(DB_Friend_Helper.CONTACTS_COLUMN_CITY));

                if (rs.isClosed())  {
                    rs.close();
                }
                Button b = (Button)findViewById(R.id.button2);
                b.setVisibility(View.VISIBLE);

                name.setText((CharSequence)nam);
                name.setFocusable(true);
                name.setClickable(true);

                phone.setText((CharSequence)phon);
                phone.setFocusable(true);
                phone.setClickable(true);

                address.setText((CharSequence)addre);
                address.setFocusable(true);
                address.setClickable(true);
            }

            back.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {
                    i.setClass(getApplicationContext(), FriendContact.class);
                    startActivity(i);


                }
            });
        }
    }

    public void run(View view) {
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){
                if(mydbf.updateContact(id_To_Update,name.getText().toString(),
                        phone.getText().toString(), address.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),FriendContact.class);
                    startActivity(intent);
                } else {
                    Toast.makeText( getApplicationContext(), "not Updated", Toast.LENGTH_SHORT ).show();
                }
            }
        }
    }
}