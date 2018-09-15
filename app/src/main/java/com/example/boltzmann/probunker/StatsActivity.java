package com.example.boltzmann.probunker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class StatsActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private BarChart mchart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Toolbar toolbar = findViewById(R.id.toolbar_stats);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        SQLiteOpenHelper proBunkerDatabaseHelper = new ProBunkerDatabaseHelper(this);
        db =proBunkerDatabaseHelper.getReadableDatabase();
        mchart = findViewById(R.id.chart);
        mchart.getDescription().setEnabled(false);
        setData();
        
    }
    private void setData(){
        ArrayList<BarEntry> yVals = new ArrayList<>();
        ArrayList<Integer> xcolors = new ArrayList<>();
        int i=0;
        try{
            Cursor cursor = db.query("MYTABLE",new String[]{"COLOR","PERCENT"},null,null,null,null,null,null);
            if(cursor!=null&&cursor.moveToFirst()){
                do{
                    float value = cursor.getFloat(cursor.getColumnIndex("PERCENT"));
                    int color = cursor.getInt(cursor.getColumnIndex("COLOR"));
                    yVals.add(new BarEntry(i,value));
                    xcolors.add(color);
                    i++;
                }while (cursor.moveToNext());
                cursor.close();
            }
        }catch (SQLiteException e){
            Toast toast = Toast.makeText(StatsActivity.this,"Unable to access database plz try again",Toast.LENGTH_SHORT);
            toast.show();
        }
        BarDataSet set = new BarDataSet(yVals,"Data Set");
        set.setColors(xcolors);
        set.setDrawValues(true);

        BarData data = new BarData(set);

        mchart.setData(data);
        mchart.invalidate();
        mchart.animateY(500);
        int maxCapacity = 75;
        LimitLine ll = new LimitLine(maxCapacity, "      75%");
        mchart.getAxisLeft().addLimitLine(ll);
    }
}
