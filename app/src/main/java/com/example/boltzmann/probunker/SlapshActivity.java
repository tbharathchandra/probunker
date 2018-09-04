package com.example.boltzmann.probunker;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SlapshActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slapsh);
        Thread background = new Thread() {
          public void run(){
              try{
                    sleep(3000);
                    Intent i = new Intent(SlapshActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
              }catch (Exception e){

              }
          }
        };
        background.start();
    }
}
