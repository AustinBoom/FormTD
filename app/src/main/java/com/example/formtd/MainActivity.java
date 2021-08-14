package com.example.formtd;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Draw view made in XML. Not much going on here.

    }

    //Disallow accidental back press!
    @Override
    public void onBackPressed() { }



}