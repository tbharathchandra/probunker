package com.example.boltzmann.probunker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProBunkerDatabaseHelper extends SQLiteOpenHelper {
   private static final int DB_version = 1;
   private static final String DB_name="probunker";
    public ProBunkerDatabaseHelper(Context context) {
        super(context,DB_name,null,DB_version );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
     sqLiteDatabase.execSQL("CREATE TABLE MYTABLE (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
             +"NAME TEXT,"
             +"TOTAL INTEGER,"
             +"BUNK INTEGER,"
             +"COLOR INTEGER,"
             +"PERCENT REAL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
