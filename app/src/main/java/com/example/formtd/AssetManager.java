package com.example.formtd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

//Holds all assets and has ability to rescale images.
public class AssetManager {
    final Handler handler;
    private Runnable runnable;
    private int tileWidth;
    private int xScale;
    private int yScale;

    //UI and other
    public Bitmap DEFAULTBITMAP;
    public Bitmap BLANKICONPLACER;
    public Bitmap TOWERDESCRIPTOR;
    public Bitmap ENEMYDESCRIPTOR;
    public Bitmap TAPTOSTART;
    public Bitmap EASY;
    public Bitmap MEDIUM;
    public Bitmap HARD;
    public Bitmap BUILD;
    public Bitmap BUILDUNPRESSED;
    public Bitmap BUILDPRESSED;
    public Bitmap REMOVE;
    public Bitmap REMOVEOFF;
    public Bitmap REMOVEON;

    //Enemies
    public Bitmap GHOST;
    public Bitmap ANT;
    public Bitmap SLEDDINGELF;
    public Bitmap WATERGHOST;
    public Bitmap HEAD;
    public Bitmap EYE;
    public Bitmap LAVAGHOST;
    public Bitmap SNEK;
    public Bitmap BABYFISHSPYENEMY;

    //Towers
    public Bitmap SNOWMANTOWER;
    public Bitmap SNOWMANTOWERICON;
    public Bitmap ARROWTOWER;
    public Bitmap ARROWTOWERICON;
    public Bitmap FISHSPY;
    public Bitmap FISHSPYICON;
    public Bitmap FISHSPYSWORD;
    public Bitmap GOLEMTOWER;
    public Bitmap GOLEMTOWERICON;
    public Bitmap BOUDLER;
    public Bitmap FRUITSTANDTOWER;
    public Bitmap FRUITSTANDTOWERICON;
    public Bitmap BANANA;
    public Bitmap CASTLETOWER;
    public Bitmap CASTLETOWERICON;
    public Bitmap CASTLEKNIGHT;

    //Projectiles
    public Bitmap ARROWPROJECTILE;

    public AssetManager(Context context, int tileWidth){
        handler = new Handler();
        this.tileWidth = tileWidth;
        this.xScale = (int)(tileWidth*1.5);
        this.yScale = (int)(tileWidth*1.5);

        /**UI**/
        //Default: used in place of a bitmap when a bitmap isn't ready!
        DEFAULTBITMAP = BitmapFactory.decodeResource(context.getResources(), R.drawable.defaultbitmap);
        DEFAULTBITMAP = Bitmap.createScaledBitmap(DEFAULTBITMAP, 1, 1, false);
        //Blank icon placer
        BLANKICONPLACER = BitmapFactory.decodeResource(context.getResources(), R.drawable.blankiconplacer);
        BLANKICONPLACER = Bitmap.createScaledBitmap(BLANKICONPLACER, 448, 448, false);
        //Tower descriptor background
        TOWERDESCRIPTOR = BitmapFactory.decodeResource(context.getResources(), R.drawable.towerdescriptor);
        TOWERDESCRIPTOR = Bitmap.createScaledBitmap(TOWERDESCRIPTOR, 448, 448, false);
        //Enemy descriptor background
        ENEMYDESCRIPTOR = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemydescriptor);
        ENEMYDESCRIPTOR = Bitmap.createScaledBitmap(ENEMYDESCRIPTOR, 448, 80, false);

        //Tap to start tooltip
        TAPTOSTART = BitmapFactory.decodeResource(context.getResources(), R.drawable.taptostart);
        TAPTOSTART = Bitmap.createScaledBitmap(TAPTOSTART, 900, 150, false);
        //Game modes:
        EASY = BitmapFactory.decodeResource(context.getResources(), R.drawable.easy);
        EASY = Bitmap.createScaledBitmap(EASY, 299, 299, false);
        MEDIUM = BitmapFactory.decodeResource(context.getResources(), R.drawable.medium);
        MEDIUM = Bitmap.createScaledBitmap(MEDIUM, 299, 299, false);
        HARD = BitmapFactory.decodeResource(context.getResources(), R.drawable.hard);
        HARD = Bitmap.createScaledBitmap(HARD, 299, 299, false);

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

        /**ENEMIES**/
        //Ant
        ANT = BitmapFactory.decodeResource(context.getResources(), R.drawable.ant);
        ANT = Bitmap.createScaledBitmap(ANT, xScale, yScale, false);
        //Ghost
        GHOST = BitmapFactory.decodeResource(context.getResources(), R.drawable.ghostenemy);
        GHOST = Bitmap.createScaledBitmap(GHOST, xScale, yScale, false);
        //Sledding Elf
        SLEDDINGELF = BitmapFactory.decodeResource(context.getResources(), R.drawable.sleddingelfenemy);
        SLEDDINGELF = Bitmap.createScaledBitmap(SLEDDINGELF, xScale, yScale, false);
        //Water ghost
        WATERGHOST = BitmapFactory.decodeResource(context.getResources(), R.drawable.waterghost);
        WATERGHOST = Bitmap.createScaledBitmap(WATERGHOST, xScale, yScale, false);
        //Head
        HEAD = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);
        HEAD = Bitmap.createScaledBitmap(HEAD, xScale, yScale, false);
        //Eye
        EYE = BitmapFactory.decodeResource(context.getResources(), R.drawable.eyeenemy);
        EYE = Bitmap.createScaledBitmap(EYE, xScale, yScale, false);
        //Lava ghost
        LAVAGHOST = BitmapFactory.decodeResource(context.getResources(), R.drawable.lavaghost);
        LAVAGHOST = Bitmap.createScaledBitmap(LAVAGHOST, xScale, yScale, false);
        //Snek
        SNEK = BitmapFactory.decodeResource(context.getResources(), R.drawable.snek);
        SNEK = Bitmap.createScaledBitmap(SNEK, xScale, yScale, false);
        //BabyFishSpy
        BABYFISHSPYENEMY = BitmapFactory.decodeResource(context.getResources(), R.drawable.fishspy);
        BABYFISHSPYENEMY = Bitmap.createScaledBitmap(BABYFISHSPYENEMY, xScale, yScale, false);


        /**TOWERS**/
        //Snowman
        SNOWMANTOWER = BitmapFactory.decodeResource(context.getResources(), R.drawable.snowman);
        SNOWMANTOWER = Bitmap.createScaledBitmap(SNOWMANTOWER, tileWidth*2, tileWidth*2, false);
        SNOWMANTOWERICON = BitmapFactory.decodeResource(context.getResources(), R.drawable.snowman);
        SNOWMANTOWERICON = Bitmap.createScaledBitmap(SNOWMANTOWERICON, DefenceView.towerIconWidth, DefenceView.towerIconWidth, false);

        //ArrowTower
        ARROWTOWER = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrowtower);
        ARROWTOWER = Bitmap.createScaledBitmap(ARROWTOWER, tileWidth*2, tileWidth*2, false);
        ARROWTOWERICON = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrowtower);
        ARROWTOWERICON = Bitmap.createScaledBitmap(ARROWTOWERICON, DefenceView.towerIconWidth, DefenceView.towerIconWidth, false);
        ARROWPROJECTILE = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrowprojectile);
        ARROWPROJECTILE = Bitmap.createScaledBitmap(ARROWPROJECTILE, 30, 30, false);

        //FishSpyTower
        FISHSPY = BitmapFactory.decodeResource(context.getResources(), R.drawable.fishspy);
        FISHSPY = Bitmap.createScaledBitmap(FISHSPY, tileWidth*2, tileWidth*2, false);
        FISHSPYICON = BitmapFactory.decodeResource(context.getResources(), R.drawable.fishspy);
        FISHSPYICON = Bitmap.createScaledBitmap(FISHSPYICON, DefenceView.towerIconWidth, DefenceView.towerIconWidth, false);
        FISHSPYSWORD = BitmapFactory.decodeResource(context.getResources(), R.drawable.fishspysword);
        FISHSPYSWORD = Bitmap.createScaledBitmap( FISHSPYSWORD, tileWidth*2, tileWidth*2, false);

        //FruitStand tower
        FRUITSTANDTOWER = BitmapFactory.decodeResource(context.getResources(), R.drawable.fruistandtower);
        FRUITSTANDTOWER = Bitmap.createScaledBitmap(FRUITSTANDTOWER, tileWidth*2, tileWidth*2, false);
        FRUITSTANDTOWERICON = BitmapFactory.decodeResource(context.getResources(), R.drawable.fruistandtower);
        FRUITSTANDTOWERICON = Bitmap.createScaledBitmap(FRUITSTANDTOWERICON, DefenceView.towerIconWidth, DefenceView.towerIconWidth, false);
        BANANA = BitmapFactory.decodeResource(context.getResources(), R.drawable.banana);
        BANANA = Bitmap.createScaledBitmap(BANANA, 20, 20, false);


        //Golem Tower
        GOLEMTOWER = BitmapFactory.decodeResource(context.getResources(), R.drawable.golemtower);
        GOLEMTOWER = Bitmap.createScaledBitmap(GOLEMTOWER, tileWidth*2, tileWidth*2, false);
        GOLEMTOWERICON = BitmapFactory.decodeResource(context.getResources(), R.drawable.golemtower);
        GOLEMTOWERICON = Bitmap.createScaledBitmap(GOLEMTOWERICON, DefenceView.towerIconWidth, DefenceView.towerIconWidth, false);
        BOUDLER = BitmapFactory.decodeResource(context.getResources(), R.drawable.boulder);
        BOUDLER = Bitmap.createScaledBitmap( BOUDLER, 80, 80, false);

        //Castle Tower
        CASTLETOWER = BitmapFactory.decodeResource(context.getResources(), R.drawable.castle);
        CASTLETOWER = Bitmap.createScaledBitmap(CASTLETOWER, tileWidth*2, tileWidth*2, false);
        CASTLETOWERICON = BitmapFactory.decodeResource(context.getResources(), R.drawable.castle);
        CASTLETOWERICON = Bitmap.createScaledBitmap(CASTLETOWERICON, DefenceView.towerIconWidth, DefenceView.towerIconWidth, false);
        CASTLEKNIGHT = BitmapFactory.decodeResource(context.getResources(), R.drawable.knight);
        CASTLEKNIGHT = Bitmap.createScaledBitmap(CASTLEKNIGHT, 60, 60, false);



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
