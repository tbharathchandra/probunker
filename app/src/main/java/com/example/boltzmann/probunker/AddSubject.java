package com.example.boltzmann.probunker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddSubject extends AppCompatActivity {
    private int mDefalutColor;
    private TextView textView;
    private String name;
    private int total;
    private int bunked;
    private float percent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Button button =  findViewById(R.id.button);
        ColorDrawable buttonColor = (ColorDrawable) button.getBackground();
        mDefalutColor = buttonColor.getColor();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPcker();
            }
        });
        Button save = findViewById(R.id.button2);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText Name = findViewById(R.id.editText);
                EditText Total = findViewById(R.id.editText2);
                EditText Bunked = findViewById(R.id.editText3);
                name = Name.getText().toString();
                total = Integer.parseInt(Total.getText().toString().trim());
                bunked = Integer.parseInt(Bunked.getText().toString().trim());
                if(!(total>=bunked)){
                    Toast toast = Toast.makeText(AddSubject.this,"Bunked classes should be less than or equal to total try again",Toast.LENGTH_SHORT);
                    toast.show();
                }else{

                    insertData();
                    Intent intent = new Intent(AddSubject.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
    public void openColorPcker(){
        AmbilWarnaDialog dailog = new AmbilWarnaDialog(this, mDefalutColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefalutColor=color;
                textView=findViewById(R.id.textView);
                textView.setBackgroundColor(mDefalutColor);
            }
        });
        dailog.show();
    }
    public void insertData(){
        SQLiteOpenHelper proBunkerDatabaseHelper = new ProBunkerDatabaseHelper(AddSubject.this);
        try{
            percent =(float) ((total-bunked)*100)/total;
            SQLiteDatabase db = proBunkerDatabaseHelper.getWritableDatabase();
            ContentValues studentValues = new ContentValues();
            studentValues.put("NAME",name);
            studentValues.put("TOTAL",total);
            studentValues.put("BUNK",bunked);
            studentValues.put("COLOR",mDefalutColor);
            studentValues.put("PERCENT",percent);
            db.insert("MYTABLE",null,studentValues);
            db.close();
        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this,"opp! please try after restarting app",Toast.LENGTH_SHORT);
            toast.show();
        }
    }
   }
