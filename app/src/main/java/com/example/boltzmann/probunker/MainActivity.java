package com.example.boltzmann.probunker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private CardAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteOpenHelper proBunkerDatabaseHelper = new ProBunkerDatabaseHelper(this);
        db = proBunkerDatabaseHelper.getReadableDatabase();
        RecyclerView recyclerView =  findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CardAdapter(this,getCursor());
        recyclerView.setAdapter(mAdapter);
        FloatingActionButton b = findViewById(R.id.fab);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddSubject.class);
                startActivity(intent);
            }
        });

    }
    public Cursor getCursor(){
        return db.query("MYTABLE",null,null,null,null,null,null);
    }
}
