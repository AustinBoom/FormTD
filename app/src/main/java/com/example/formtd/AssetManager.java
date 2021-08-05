package com.example.formtd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;


import java.util.Timer;
import java.util.TimerTask;

import static android.os.SystemClock.sleep;

//Holds all assets and has ability to rescale images.
public class AssetManager {
    final Handler handler;
    private Runnable runnable;
    public Bitmap BUILD;
    public Bitmap BUILDUNPRESSED;
    public Bitmap BUILDPRESSED;

    public AssetManager(Context context){
        handler = new Handler();

        /**Assets**/
        //Build Button
        BUILD = BitmapFactory.decodeResource(context.getResources(), R.drawable.build);
        BUILD = Bitmap.createScaledBitmap(BUILD, 250, 80, false);
        BUILDUNPRESSED = BitmapFactory.decodeResource(context.getResources(), R.drawable.build);
        BUILDUNPRESSED = Bitmap.createScaledBitmap(BUILD, 250, 80, false);
        BUILDPRESSED = BitmapFactory.decodeResource(context.getResources(), R.drawable.buildpressed);
        BUILDPRESSED = Bitmap.createScaledBitmap(BUILDPRESSED, 249, 79, false);
    }

    public void buildPressed(){
        BUILD = BUILDPRESSED;
        runnable = new Runnable() {
            public void run() {
                BUILD = BUILDUNPRESSED;
            }
        };
        handler.postDelayed(runnable, 60); //How long the button glows pressed.
    }

}
