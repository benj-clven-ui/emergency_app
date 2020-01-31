package com.myapp.emergency;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class Contacts extends Activity
{
    Intent i = new Intent();
    Button b1;
    Button b2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contact);

        b1 = (Button)findViewById(R.id.fam);
        b2 = (Button)findViewById(R.id.friend);


        Button back = (Button) findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                i.setClass(getApplicationContext(), MainActivity.class);
                startActivity(i);


            }
        });


        b1.setOnClickListener(

                new View.OnClickListener(){

                    @Override
                    public void onClick(View p1)
                    {
                        // TODO: Implement this method

                        Intent i = new Intent(Contacts.this,FamilyContacts.class);
                        startActivity(i);

                    }
                }

        );

        b2.setOnClickListener(

        new View.OnClickListener(){

            @Override
            public void onClick(View p1)
            {
                // TODO: Implement this method

                Intent i = new Intent(Contacts.this,FriendContact.class);
                startActivity(i);

            }
        }

        );
    }
}