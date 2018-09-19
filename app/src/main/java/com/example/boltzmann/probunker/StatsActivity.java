package com.example.boltzmann.probunker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

public class StatsActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private BarChart mchart;
    private TextView all65;
    private TextView all75;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Toolbar toolbar = findViewById(R.id.toolbar_stats);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713");
        mAdView = (AdView) findViewById(R.id.ad2);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("03FDF9C1F3691D99070BA570B8DEBB21").build();
        mAdView.loadAd(adRequest);

        SQLiteOpenHelper proBunkerDatabaseHelper = new ProBunkerDatabaseHelper(this);
        db =proBunkerDatabaseHelper.getReadableDatabase();

        mchart = findViewById(R.id.chart);
        all65 =findViewById(R.id.all_65);
        all75 = findViewById(R.id.all_75);

        mchart.getDescription().setEnabled(false);
        setData();
        setAllBunks();



    }
    private void setData(){
        ArrayList<BarEntry> yVals = new ArrayList<>();
        ArrayList<Integer> xcolors = new ArrayList<>();
        final ArrayList<String> labels = new ArrayList<>();
        int i=0;
        try{
            Cursor cursor = db.query("MYTABLE",new String[]{"COLOR","PERCENT","NAME"},null,null,null,null,null,null);
            if(cursor!=null&&cursor.moveToFirst()){
                do{
                    float value = cursor.getFloat(cursor.getColumnIndex("PERCENT"));
                    int color = cursor.getInt(cursor.getColumnIndex("COLOR"));
                    String label = cursor.getString(cursor.getColumnIndex("NAME"));
                    yVals.add(new BarEntry(i,value));
                    xcolors.add(color);
                    labels.add(label);
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
        XAxis xAxis = mchart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get((int) value);
            }
        });
    }
    private int forN(int total,int bunked,float N){
        int attended = total-bunked;
        return (int) ((attended/N)-total-2);
    }
    private void setAllBunks(){
        int total=0;
        int bunked=0;
        try{
            Cursor cursor1 = db.query("MYTABLE",new String[] {"TOTAL","BUNK"},null,null,null,null,null,null);
            if(cursor1!=null&&cursor1.moveToFirst()){

                do{
                    total = total+cursor1.getInt(cursor1.getColumnIndex("TOTAL"));
                    bunked = bunked+cursor1.getInt(cursor1.getColumnIndex("BUNK"));
                }while(cursor1.moveToNext());
                cursor1.close();
            }

        }catch(SQLiteException e){
            Toast toast = Toast.makeText(StatsActivity.this,"unable to access database",Toast.LENGTH_SHORT);
            toast.show();
        }

        int no65 = forN(total,bunked,(float)0.65);
        int no75 = forN(total,bunked,(float)0.75);
        if(!(no75<1)) {
            all65.setText(Integer.toString(no65));
            all75.setText(Integer.toString(no75));
        }else {
            all65.setText("0");
            all75.setText("0");
        }
    }
}
