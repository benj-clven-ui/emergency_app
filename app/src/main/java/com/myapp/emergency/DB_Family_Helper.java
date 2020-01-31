package com.myapp.emergency;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import java.util.*;

public class DB_Family_Helper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_CITY = "place";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    private HashMap hp;


    public DB_Family_Helper(Context context) {
        super( context, DATABASE_NAME, null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table contacts " +
                        "(id integer primary key AUTOINCREMENT, name text,phone text,place text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL( "DROP TABLE IF EXISTS contacts" );
        onCreate( db );
    }

    public boolean insertContact(String name, String phone, String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( "name", name );
        contentValues.put( "phone", phone );
        contentValues.put( "place", place );
        db.insert( "contacts", null, contentValues );
        return true;
    }


    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "select * from contacts where id=" + id + "", null );
        return res;
    }

    public Cursor getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor resf = db.rawQuery( "select * from contacts where id=" + id, null );
        return resf;
    }


    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries( db, CONTACTS_TABLE_NAME );
        return numRows;
    }

    public boolean updateContact(Integer id, String name, String phone, String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( "name", name );
        contentValues.put( "phone", phone );
        contentValues.put( "place", place );
        db.update( "contacts", contentValues, "id = ? ", new String[]{Integer.toString( id )} );
        return true;
    }


    public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete( "contacts",
                "id = ? ",
                new String[]{Integer.toString( id )} );
    }


    public ArrayList<String> getAllContacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            String name = res.getString( res.getColumnIndex( CONTACTS_COLUMN_NAME ) );
            String phone =  res.getString( res.getColumnIndex( CONTACTS_COLUMN_PHONE ) );
            array_list.add(name + " "  + "\n" + " " + phone);
            res.moveToNext();
        }
        return array_list;
    }
}