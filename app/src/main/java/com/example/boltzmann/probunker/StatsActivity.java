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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    private StatsAdapter mAdapter;
    private AdView mAdView;
    private EditText min_percent;
    private Button calc;
    private TextView total_bunks_available;
    private int minPercent = 75;
    private RecyclerView recyclerView;
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
        mchart.getDescription().setEnabled(false);
        setData();


        min_percent = findViewById(R.id.min_percent);
        calc = findViewById(R.id.save_min_percent);
        total_bunks_available = findViewById(R.id.total_bunks_available);
        setTotal();

        recyclerView = findViewById(R.id.rv_stats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        float N = (float) minPercent/100;
        mAdapter = new StatsAdapter(this,getCursor(),N);
        recyclerView.setAdapter(mAdapter);


        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String minString = min_percent.getText().toString().trim();
                minPercent = Integer.parseInt(minString);
                if ( minPercent < 100 && !(minString.matches(""))) {
                    setBunks();
                    setTotal();
                }else{
                    Toast toast = Toast.makeText(StatsActivity.this,"Enter a valid percentage",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

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
        return (int) ((attended/N)-total-1);
    }
    private void setBunks(){
        float N = (float) minPercent/100;
        mAdapter = new StatsAdapter(this,getCursor(),N);
        recyclerView.setAdapter(mAdapter);
    }
    private Cursor getCursor(){
        return db.query("MYTABLE",new String[]{"NAME","TOTAL","BUNK"},null,null,null,null,null);
    }
    private void setTotal(){
        Cursor total = db.query("MYTABLE",new String[]{"TOTAL","BUNK"},null,null,null,null,null);
        if(total!=null&&total.moveToFirst()){
            int sumTotal=0;
            int sumBunked=0;
            do{
                int singleTotal = total.getInt(total.getColumnIndex("TOTAL"));
                int singleBunk = total.getInt(total.getColumnIndex("BUNK"));
                sumTotal = sumTotal+singleTotal;
                sumBunked = sumBunked+singleBunk;
            }while(total.moveToNext());
            float N = (float) minPercent/100;
            int avai_total = forN(sumTotal,sumBunked,N);
            if(avai_total<1){
                avai_total=0;
            }
            total_bunks_available.setText(String.valueOf(avai_total));
            total.close();
    }
   }

}
