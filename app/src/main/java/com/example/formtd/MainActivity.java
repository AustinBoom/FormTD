package com.example.formtd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Draw view made in XML. Not much going on here.
        //todo make a drawable

        TextView view = new TextView(this);
        view.setText("click me");
        view.setTextColor(0xffcccccc);
        view.setGravity(Gravity.CENTER);
        view.setTextSize(48);
        final EnemyDrawable d = new EnemyDrawable();
        view.setBackgroundDrawable(d);
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.startAnimating();
            }
        };
        view.setOnClickListener(l);
        setContentView(view);
    }
}