package com.example.boltzmann.probunker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddSubject.class);
                startActivity(intent);
            }
        });
        SQLiteOpenHelper proBunkerDatabaseHelper = new ProBunkerDatabaseHelper(this);
        try{
            SQLiteDatabase db = proBunkerDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("MYTABLE",new String[] {"NAME"},null,null,null,null,null);
            if(cursor.moveToFirst()){
                String Nmaetext = cursor.getString(0);
                TextView sample = findViewById(R.id.sample_text_view);
                sample.setText(Nmaetext);
            }
            cursor.close();
            db.close();
        }catch (SQLiteException e){
           Toast toast = Toast.makeText(this,"unable to show records",Toast.LENGTH_SHORT);
           toast.show();
        }
    }
}
