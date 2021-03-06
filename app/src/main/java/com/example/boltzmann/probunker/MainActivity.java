package com.example.boltzmann.probunker;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private CardAdapter mAdapter;
    private TextView totalPercent;
    private int sumTotal;
    private int sumBunked;
    private Cursor total;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713");
        mAdView = (AdView) findViewById(R.id.ad1);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("03FDF9C1F3691D99070BA570B8DEBB21").build();
        mAdView.loadAd(adRequest);
        mAdView.bringToFront();

        totalPercent = findViewById(R.id.total_percent);
        SQLiteOpenHelper proBunkerDatabaseHelper = new ProBunkerDatabaseHelper(this);
        db = proBunkerDatabaseHelper.getWritableDatabase();
        setPercentage();
        RecyclerView recyclerView =  findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CardAdapter(this, getCursor(), new CardAdapter.butttonsAdapetrListener() {
            @Override
            public void deleteOnClick(int id,long position) {
                dailog(id);
            }

            @Override
            public void plusTotalOnClick(int id, long adapterpos) {
                Cursor data = db.query("MYTABLE",new String[]{"TOTAL","BUNK"},"_id=?",new String[]{Integer.toString(id)},null,null,null);
                data.moveToFirst();
                int total = data.getInt(data.getColumnIndex("TOTAL"));
                int bunk = data.getInt(data.getColumnIndex("BUNK"));
                total=total+1;

                    float percent =(float) ((total-bunk)*100)/total;
                    try{
                        ContentValues values = new ContentValues();
                        values.put("TOTAL",total);
                        values.put("PERCENT",percent);
                        db.update("MYTABLE",values,"_id=?",new String[]{Integer.toString(id)});
                        data.close();

                    }catch(SQLiteException e){
                        Toast toast = Toast.makeText(MainActivity.this,"unable to access database plz try after restarting the app",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    mAdapter.swapCursor(getCursor());
                    data.close();
                    setPercentage();
            }

            @Override
            public void minusTotalOnClick(int id, long adapterpos) {
                Cursor data = db.query("MYTABLE",new String[]{"TOTAL","BUNK"},"_id=?",new String[]{Integer.toString(id)},null,null,null);
                data.moveToFirst();
                int total = data.getInt(data.getColumnIndex("TOTAL"));
                int bunk = data.getInt(data.getColumnIndex("BUNK"));
                total=total-1;

                    float percent =(float) ((total-bunk)*100)/total;
                   try{
                       ContentValues values = new ContentValues();
                       values.put("TOTAL",total);
                       values.put("PERCENT",percent);
                       db.update("MYTABLE",values,"_id=?",new String[]{Integer.toString(id)});
                       data.close();

                   }catch(SQLiteException e){
                       Toast toast = Toast.makeText(MainActivity.this,"unable to access database plz try after restarting the app",Toast.LENGTH_SHORT);
                       toast.show();
                    }
                    mAdapter.swapCursor(getCursor());
                   data.close();
                    setPercentage();

            }

            @Override
            public void plusBunkOnClick(int id, long adapterpos) {
                Cursor data = db.query("MYTABLE",new String[]{"BUNK","TOTAL"},"_id=?",new String[]{Integer.toString(id)},null,null,null);
                data.moveToFirst();
                int bunk = data.getInt(data.getColumnIndex("BUNK"));
                int total = data.getInt(data.getColumnIndex("TOTAL"));
                bunk = bunk+1;
                float percent =(float) ((total-bunk)*100)/total;
                ContentValues values = new ContentValues();
                values.put("BUNK",bunk);
                values.put("PERCENT",percent);
                db.update("MYTABLE",values,"_id=?",new String[]{Integer.toString(id)});
                data.close();
                mAdapter.swapCursor(getCursor());
                data.close();
                setPercentage();
            }

            @Override
            public void minusBunkOnClick(int id, long adapterpos) {
                Cursor data = db.query("MYTABLE",new String[]{"BUNK","TOTAL"},"_id=?",new String[]{Integer.toString(id)},null,null,null);
                data.moveToFirst();
                int bunk = data.getInt(data.getColumnIndex("BUNK"));
                int total = data.getInt(data.getColumnIndex("TOTAL"));
                bunk = bunk-1;
                float percent =(float) ((total-bunk)*100)/total;
                ContentValues values = new ContentValues();
                values.put("BUNK",bunk);
                values.put("PERCENT",percent);
                db.update("MYTABLE",values,"_id=?",new String[]{Integer.toString(id)});
                data.close();
                mAdapter.swapCursor(getCursor());
                data.close();
                setPercentage();
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.open_charts :
                Intent intent = new Intent(MainActivity.this,StatsActivity.class);
                startActivity(intent);
                return true;
            case R.id.open_about :
                Intent intent2 = new Intent(MainActivity.this,AboutUsActivity.class);
                startActivity(intent2);
                return true;
            case R.id.open_licenses :
                Intent intent3 = new Intent(MainActivity.this,ExternalLibraries.class);
                startActivity(intent3);
                return true;
            case R.id.share :
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"I am using ProBunker app and it is just awesome.., try using it");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Cursor getCursor(){
        return db.query("MYTABLE",null,null,null,null,null,"_id ASC");
    }
    public void dailog(final int id){
        AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);
        a_builder.setMessage("Do you want to delete this subject?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.delete("MYTABLE","_id="+id,null);
                        mAdapter.swapCursor(getCursor());
                        setPercentage();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Alert!!");
        alert.show();
    }
    public void setPercentage(){
        sumTotal=0;
        sumBunked=0;
        try{
            total = db.query("MYTABLE",new String[]{"TOTAL","BUNK"},null,null,null,null,null);
            if(total!=null&&total.moveToFirst()){

            do{
                int singleTotal = total.getInt(total.getColumnIndex("TOTAL"));
                int singleBunk = total.getInt(total.getColumnIndex("BUNK"));
                sumTotal = sumTotal+singleTotal;
                sumBunked = sumBunked+singleBunk;
            }while(total.moveToNext());
            float myPercent = (float) ((sumTotal-sumBunked)*100)/sumTotal;
            String percentage = String.format("%.2f",myPercent)+"%";
            totalPercent.setText(percentage);
            totalPercent.setTextSize(40);
            total.close();}else{
                totalPercent.setText("Add subjects");
                totalPercent.setTextSize(20);

            }
        }catch (SQLiteException e){
            Toast toast =Toast.makeText(MainActivity.this,"unable to access database, try restarting app",Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
