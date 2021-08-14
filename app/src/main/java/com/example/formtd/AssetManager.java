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
    private int tileWidth;
    private int xScale;
    private int yScale;

    //UI and other
    public Bitmap BLANKICONPLACER;
    public Bitmap TAPTOSTART;
    public Bitmap BUILD;
    public Bitmap BUILDUNPRESSED;
    public Bitmap BUILDPRESSED;
    public Bitmap REMOVE;
    public Bitmap REMOVEOFF;
    public Bitmap REMOVEON;

    //Enemies
    public Bitmap GHOST;
    public Bitmap MINER;

    //Towers
    public Bitmap SNOWMAN;
    public Bitmap SNOWMANICON;
    public Bitmap ARROWTOWER;
    public Bitmap ARROWTOWERICON;

    public AssetManager(Context context, int tileWidth){
        handler = new Handler();
        this.tileWidth = tileWidth;
        this.xScale = (int)(tileWidth*1.5);
        this.yScale = (int)(tileWidth*1.5);

        /**Assets**/
        //Blank icon placer
        BLANKICONPLACER = BitmapFactory.decodeResource(context.getResources(), R.drawable.blankiconplacer);
        BLANKICONPLACER = Bitmap.createScaledBitmap(BLANKICONPLACER, 448, 448, false);

        //Tap to start tooltip
        TAPTOSTART = BitmapFactory.decodeResource(context.getResources(), R.drawable.taptostart);
        TAPTOSTART = Bitmap.createScaledBitmap(TAPTOSTART, 900, 150, false);

        //Build Button
        BUILD = BitmapFactory.decodeResource(context.getResources(), R.drawable.build);
        BUILD = Bitmap.createScaledBitmap(BUILD, 250, 80, false);
        BUILDUNPRESSED = BitmapFactory.decodeResource(context.getResources(), R.drawable.build);
        BUILDUNPRESSED = Bitmap.createScaledBitmap(BUILD, 250, 80, false);
        BUILDPRESSED = BitmapFactory.decodeResource(context.getResources(), R.drawable.buildpressed);
        BUILDPRESSED = Bitmap.createScaledBitmap(BUILDPRESSED, 249, 79, false); //249 to make it look shrinked when pressed

        //Remove button
        REMOVE = BitmapFactory.decodeResource(context.getResources(), R.drawable.removeoff);
        REMOVE = Bitmap.createScaledBitmap(REMOVE, 80, 80, false);
        REMOVEOFF = BitmapFactory.decodeResource(context.getResources(), R.drawable.removeoff);
        REMOVEOFF = Bitmap.createScaledBitmap(REMOVEOFF, 80, 80, false);
        REMOVEON = BitmapFactory.decodeResource(context.getResources(), R.drawable.removeon);
        REMOVEON = Bitmap.createScaledBitmap(REMOVEON, 80, 80, false);

        //Ghost
        GHOST = BitmapFactory.decodeResource(context.getResources(), R.drawable.ghostenemy);
        GHOST = Bitmap.createScaledBitmap(GHOST, xScale, yScale, false);
        MINER = BitmapFactory.decodeResource(context.getResources(), R.drawable.minerenemy);
        MINER = Bitmap.createScaledBitmap(MINER, xScale, yScale, false);

        //Snowman
        SNOWMAN = BitmapFactory.decodeResource(context.getResources(), R.drawable.snowman);
        SNOWMAN = Bitmap.createScaledBitmap(SNOWMAN, tileWidth*2, tileWidth*2, false);
        SNOWMANICON = BitmapFactory.decodeResource(context.getResources(), R.drawable.snowman);
        SNOWMANICON = Bitmap.createScaledBitmap(SNOWMANICON, DefenceView.towerIconWidth, DefenceView.towerIconWidth, false);
        //ArrowTower
        ARROWTOWER = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrowtower);
        ARROWTOWER = Bitmap.createScaledBitmap(ARROWTOWER, tileWidth*2, tileWidth*2, false);
        ARROWTOWERICON = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrowtower);
        ARROWTOWERICON = Bitmap.createScaledBitmap(ARROWTOWERICON, DefenceView.towerIconWidth, DefenceView.towerIconWidth, false);



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

    public void removeON(){
        REMOVE = REMOVEON;
    }

    public void removeOFF(){
        REMOVE = REMOVEOFF;
    }


}
