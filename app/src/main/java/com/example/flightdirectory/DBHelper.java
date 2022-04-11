package com.example.flightdirectory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contactsDb";
    public static final String TABLE_CONTACTS = "contacts";

    public static final String KEY_ID1 = "_id1";
    public static final String KEY_START = "startRoute";
    public static final String KEY_END = "endRoute";
    public static final String KEY_PRICE = "date";

    public static final String TABLE_USERS = "userTable";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASSWORD = "password";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID1
                + " integer primary key," + KEY_START + " text," + KEY_END + " text," + KEY_PRICE + " text" + ")");

        db.execSQL("create table " + TABLE_USERS + "(" + KEY_ID
                + " integer primary key," + KEY_NAME + " text," + KEY_PASSWORD + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion > newVersion){
            db.execSQL("drop table if exists " + TABLE_CONTACTS);
            onCreate(db);

            db.execSQL("drop table if exists " + TABLE_USERS);
            onCreate(db);
        }
    }
}