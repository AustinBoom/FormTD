/**
 * @author Austin Huyboom.
 *
 */

package com.example.formtd;
import com.example.formtd.towers.*;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class DefenceView extends View implements View.OnTouchListener {
    //Boiler Plate
    Context context;

    //World
    public static Grid[][] grid;                    //The grid which is the world!
    public static int deviceWidth;
    public static int deviceHeight;
    public static int centerXGrid;
    public static int tileWidth;
    public static int xGridStart;
    public static int yGridStart;
    public static int xGridEnd;
    public static int yGridEnd;
    AssetManager asset;
    GridManager gridManager;                        //Creates/updates grid
    HighlightManager highlightManager;              //Manages placement via touch.
    PlacementManager placementManager;              //Manages existing towers and spot availability.
    public static ArrayList<Tower> towers;          //Towers are created/removed here, and managed in Wave class.
    public static boolean pathNeedsUpdating = false;    //Whenever a tower is placed, set this to true and Wave class will know about it.
    Paint paint;

    //Mechanics
    StaticLayout staticLayout;      //For text
    TextPaint textPaint = new TextPaint();
    final int textSize = 32;
    ArrayList<Wave> wave;            //Holds every wave that exists
    public boolean begin = false;   //When game has begun
    protected int waveTimer = 3000;           //Time between waves (ex. 60000ms = 60 seconds)
    protected int countdown = 0;              //Countdown timer. Set to waveTimer/1000 then counts down each wave. (do not set here)
    public static boolean gameOver = false;
    public static boolean lastWave = false;
    public static int currentWave = 0;
    public static int lives = 50;
    public static int gold = 1000;
    public static boolean blocking = false;
    public static int waveID = 0;       //Global id assigned to enemies. Used to count up for uniqueness. (don't need heavy duty like UUID)


    public DefenceView(Context context) {
        super(context);
        this.context = context;
        setOnTouchListener(this);
        towers = new ArrayList<>();
        paint = new Paint();
    }

    public DefenceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        setOnTouchListener(this);
        towers = new ArrayList<>();
        paint = new Paint();
    }



    //CRUCIAL measurement, this method initializes a GridManager
    protected void onMeasure(int widthMeasure, int heightMeasure){
        deviceWidth = MeasureSpec.getSize(widthMeasure);
        deviceHeight = MeasureSpec.getSize(heightMeasure);

        gridManager = new GridManager(deviceWidth, deviceHeight);
        grid = gridManager.getGrid();
        centerXGrid = gridManager.getXCenterGridCoordinate();
        tileWidth = gridManager.getTileWidth();
        xGridStart = gridManager.getxGridStart();
        yGridStart = gridManager.getyGridStart();
        xGridEnd = gridManager.getxGridEnd();
        yGridEnd = gridManager.getyGridEnd();
        asset = new AssetManager(context, gridManager.getTileWidth());
        highlightManager = new HighlightManager(grid, gridManager.getTileWidth(), gridManager.getxGridStart(), gridManager.getyGridStart());
        placementManager = new PlacementManager(grid, gridManager.getTileWidth());


        initWaves();        //Set up waves now that dimensions are in place.

        //Boiler plate. Removing this is CATASTROPHIC!
        setMeasuredDimension(deviceWidth, deviceHeight);
    }

    

    //When screen is touched, respond!
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if(!begin){     //ONE TIME BEGINNING STUFF HERE. Once screen is touched start certain actions.
                begin = true;
                startWaves();
            }
            highlightManager.setHighlightPlacement((int)motionEvent.getX(), (int)motionEvent.getY());

            if(ifTouchIsInBuildButton(motionEvent)){
                asset.buildPressed();
                if(placementManager.checkSpotAvailability(highlightManager.getHighlightPlacement())){
                    towers.add(new SnowballTower(highlightManager.getHighlightPlacement(), placementManager));
                    for (int i = 0; i < wave.size(); i++) {
                        wave.get(i).pathNeedsUpdating = true;
                    }
                }
            }

            if(ifTouchIsInRemoveButton(motionEvent)){
                //Check each tower if it's the one that's highlighted
                for (int i = 0; i < towers.size(); i++) {
                    if(towers.get(i).selectTower(highlightManager.getHighlightPlacement())){
                        placementManager.removeTower(highlightManager.getHighlightPlacement()); //Remove tower from grid
                        towers.remove(i);   //Remove tower from towers arraylist
                        asset.removeOFF();
                        for (int j = 0; j < wave.size(); j++) {
                            wave.get(j).pathNeedsUpdating = true;
                        }
                    }
                }
            }

        }

        invalidate();       //update drawings.
        return true;       //false: don't listen for subsequent events (change to true for something like a double tap)
    }



    //Draw listener that updates the view. (NOTE: order of drawing matters!)
    @Override
    public void onDraw(Canvas canvas){
        //Just to test drawview
        gridManager.drawGrid(canvas);
        drawTowers(canvas);
        //drawTestBall(canvas);
        drawHighLight(canvas);
        drawCurrentWave(canvas);
        drawTowerProjectiles(canvas);
        drawUI(canvas);
        drawInfoBarStuff(canvas);


        //invalidate();       //PUT SOMEWHERE ELSE. This makes drawview update
    }


    //Draw the highlight from the point tapped on the map
    private void drawHighLight(Canvas canvas){
        //Get the highlighted points. If null then there is no highlight to draw; simply do nothing.
        RectanglePoints rectanglePoints = highlightManager.getHighlightPlacement();
        if(rectanglePoints != null){
            if(placementManager.checkSpotAvailability(rectanglePoints.left, rectanglePoints.top))
                paint.setARGB(85, 50, 255, 50);
            else
                paint.setARGB(120, 255, 50, 50);

            canvas.drawRect(rectanglePoints.left, rectanglePoints.top, rectanglePoints.right, rectanglePoints.bottom, paint);
        }
    }

    private void drawTowers(Canvas canvas){
        for (int i = 0; i < towers.size(); i++) {
            towers.get(i).drawTower(canvas);
        }
    }

    private void drawTowerProjectiles(Canvas canvas){
        for (int i = 0; i < towers.size(); i++) {
            towers.get(i).drawProjectile(canvas);
        }
    }

    private void drawUI(Canvas canvas){
        //Begin text
        if(!begin) {
            //First rect is shadow, second is actual tap to start
            paint.setARGB(40, 5, 5, 5);
            canvas.drawRect(deviceWidth / 2 - (asset.TAPTOSTART.getWidth() / 2) + 12, deviceHeight / 6 - (asset.TAPTOSTART.getHeight() / 2) + 17, asset.TAPTOSTART.getWidth() + deviceWidth / 2 - (asset.TAPTOSTART.getWidth() / 2) + 12, asset.TAPTOSTART.getHeight() + deviceHeight / 6 - (asset.TAPTOSTART.getHeight() / 2) + 17, paint);
            paint.setARGB(200, 255, 255, 255);
            canvas.drawBitmap(asset.TAPTOSTART, deviceWidth / 2 - (asset.TAPTOSTART.getWidth() / 2), deviceHeight / 6 - (asset.TAPTOSTART.getHeight() / 2), paint);
        }
        //Remove button highlight on
        for (int i = 0; i < towers.size(); i++) {
            if(towers.get(i).selectTower(highlightManager.getHighlightPlacement())){
                asset.removeON();
                break;
            }
            else{
                asset.removeOFF();
            }
        }

        //Info bar
        paint.setARGB(255, 50, 70, 90);
        canvas.drawRect(0, 0, deviceWidth, deviceHeight/40, paint);

        //Build Button
        canvas.drawBitmap(asset.BUILD, deviceWidth - gridManager.getxGridStart()*2 - (asset.BUILD.getWidth()) - (asset.REMOVE.getWidth()), grid[grid.length-1][0].y + (gridManager.getTileWidth() + gridManager.getTileWidth()/2), null);
        //Remove button
        canvas.drawBitmap(asset.REMOVE, deviceWidth - gridManager.getxGridStart() - (asset.REMOVE.getWidth()), grid[grid.length-1][0].y + (gridManager.getTileWidth() + gridManager.getTileWidth()/2), null);

    }

    private void drawInfoBarStuff(Canvas canvas){
        //Set the text of the wave timer
        textPaint.setARGB(255, 200, 200, 200);
        textPaint.setTextSize(textSize);
        staticLayout = new StaticLayout("Wave in: " + countdown, textPaint, 200, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        canvas.save();
        canvas.translate( gridManager.getxGridStart(), deviceHeight/80 - textSize/2);
        staticLayout.draw(canvas);
        canvas.restore();

        //Set the text of the current wave
        staticLayout = new StaticLayout("Wave: " + currentWave, textPaint, 150, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        canvas.save();
        canvas.translate( deviceWidth*7/20 - staticLayout.getWidth()/2, deviceHeight/80 - textSize/2);
        staticLayout.draw(canvas);
        canvas.restore();

        //Set the text of the life meter
        staticLayout = new StaticLayout("Lives: " + lives, textPaint, 150, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        canvas.save();
        canvas.translate( deviceWidth*3/5 - staticLayout.getWidth()/2, deviceHeight/80 - textSize/2);
        staticLayout.draw(canvas);
        canvas.restore();

        //Set the text of the current gold
        staticLayout = new StaticLayout("Gold: " + gold, textPaint, 250, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        canvas.save();
        canvas.translate( deviceWidth*3/4, deviceHeight/80 - textSize/2);
        staticLayout.draw(canvas);
        canvas.restore();

        if(blocking) {
            //Set the blocking text
            textPaint.setARGB(120, 255, 10, 10);
            textPaint.setTextSize(textSize + 10);
            staticLayout = new StaticLayout("PATH BLOCKED!", textPaint, 340, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
            canvas.save();
            canvas.translate(deviceWidth / 2 - staticLayout.getWidth() / 2, yGridStart * 2);
            staticLayout.draw(canvas);
            canvas.restore();
        }

        //invalidate();
    }

    private boolean ifTouchIsInBuildButton(MotionEvent motionEvent){
        return deviceWidth - gridManager.getxGridStart()*2 - (asset.BUILD.getWidth()) - (asset.REMOVE.getWidth()) < motionEvent.getX() && motionEvent.getX() < deviceWidth - gridManager.getxGridStart()*2 - (asset.REMOVE.getWidth())
                && grid[grid.length - 1][0].y + (gridManager.getTileWidth() + gridManager.getTileWidth() / 2) < motionEvent.getY() && motionEvent.getY() < grid[grid.length - 1][0].y + (gridManager.getTileWidth() + gridManager.getTileWidth() / 2) + asset.BUILD.getHeight();
    }

    private boolean ifTouchIsInRemoveButton(MotionEvent motionEvent){
        return deviceWidth - gridManager.getxGridStart() - (asset.REMOVE.getWidth()) < motionEvent.getX() && motionEvent.getX() < deviceWidth - gridManager.getxGridStart()
                && grid[grid.length - 1][0].y + (gridManager.getTileWidth() + gridManager.getTileWidth() / 2) < motionEvent.getY() && motionEvent.getY() < grid[grid.length - 1][0].y + (gridManager.getTileWidth() + gridManager.getTileWidth() / 2) + asset.BUILD.getHeight();
    }

    private void initWaves(){
        wave = new ArrayList<>();

        //Add waves. These are how the levels are designed.
        wave.add(new Wave(asset, "ghost", 1, waveID++));
        wave.add(new Wave(asset, "ghost",2, waveID++));
        wave.add(new Wave(asset, "ghost", 8, waveID++));

    }

    //Updates the current wave when the time has come. If waves run out, game is over.
    private void startWaves(){
        final Timer timer = new Timer();
        TimerTask timerTask =  new TimerTask(){
            @Override
            public void run(){
                if(currentWave < wave.size() && !gameOver) {
                    if(currentWave == wave.size()-1)
                        lastWave = true;
                    wave.get(currentWave).startWave();
                    wave.get(currentWave).active = true;
                    currentWave++;
                }
                else{
                    //TODO put end of game stuff here
                    gameOver = true;
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, waveTimer, waveTimer);  //Start wave
        startWaveTimer();                                           //Start wave timer
    }



    private void drawCurrentWave(Canvas canvas){
        for (Wave wave: wave) {     //for each wave in wave,
            if(wave.active){
                wave.drawWave(canvas);
                invalidate();
            }
        }
    }

    private void startWaveTimer(){
        countdown = waveTimer/1000;
        final Timer timerCountdown = new Timer();
        TimerTask timerTaskCnt =  new TimerTask(){
            @Override
            public void run(){
                if(countdown > 1 && !gameOver && !lastWave) {
                    countdown--;
                }
                else if(gameOver || lastWave){
                    countdown = 0;
                    timerCountdown.cancel();
                }
                else{
                    countdown = waveTimer/1000;  //Reset timer for next round
                }
                invalidate();
            }
        };
        timerCountdown.scheduleAtFixedRate(timerTaskCnt, 1000, 1000);  //Every second.
    }

}


/** Boneyard: Where old ideas may rise once more

    ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.ALPHA, 0.1f, 0.9f);
    animator.setDuration(250);
    // set all the animation-related stuff you want (interpolator etc.)
    animator.start();


    Timer timer = new Timer();
    TimerTask update = new UpdateTask();
    timer.scheduleAtFixedRate(update, 1000, 1000);
    //Make this do something it needs to do
    class UpdateTask extends TimerTask {
        @Override
        public void run() {
            //   y += 1;
        }
     }
 **/