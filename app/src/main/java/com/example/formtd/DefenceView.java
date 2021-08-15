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
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class DefenceView extends View implements View.OnTouchListener {
    //Boiler Plate
    Context context;

    //World & Layout
    public static Grid[][] grid;                    //The grid which is the world!
    public static int deviceWidth;
    public static int deviceHeight;
    public static int centerXGrid;
    public static int tileWidth;
    public static int xGridStart;
    public static int yGridStart;
    public static int xGridEnd;
    public static int yGridEnd;
    public int buildButtonX;
    public int buildButtonY;
    public int removeButtonX;
    public int removeButtonY;
    public int blankIconPlacerX;
    public int blankIconPlacerY;
    public int towerDescriptorX;
    public int towerDescriptorY;
    public int currentTowerIconHighlightX;
    public int currentTowerIconHighlightY;
    public int towerIconOneX;
    public int towerIconOneY;
    public int towerIconTwoX;
    public int towerIconTwoY;
    public int towerIconThreeX;
    public int towerIconThreeY;
    public int towerIconFourX;
    public int towerIconFourY;
    public int towerIconFiveX;
    public int towerIconFiveY;
    public int towerIconSixX;
    public int towerIconSixY;
    public int towerIconSevenX;
    public int towerIconSevenY;
    public int towerIconEightX;
    public int towerIconEightY;
    public int towerIconNineX;
    public int towerIconNineY;
    public static final int towerIconWidth = 146;   //To fit inside box.
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
    protected int waveTimer = 50000;           //Time between waves (ex. 60000ms = 60 seconds)
    protected int countdown = 0;              //Countdown timer. Set to waveTimer/1000 then counts down each wave. (do not set here)
    public static boolean gameOver = false;
    public static boolean lastWave = false;
    public static int currentWave = 0;
    public static int lives = 50;
    public static int gold = 125;
    public static float returnRate = 0.75f;
    public static boolean blocking = false;
    public static int waveID = 0;       //Global id assigned to enemies. Used to count up for uniqueness. (don't need heavy duty like UUID)
    public int selectedTowerIcon = 1;


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

        //Grid manager
        gridManager = new GridManager(deviceWidth, deviceHeight);
        grid = gridManager.getGrid();
        asset = new AssetManager(context, gridManager.getTileWidth());

        //Needed measurements
        centerXGrid = gridManager.getXCenterGridCoordinate();
        tileWidth = gridManager.getTileWidth();
        xGridStart = gridManager.getxGridStart();
        yGridStart = gridManager.getyGridStart();
        xGridEnd = gridManager.getxGridEnd();
        yGridEnd = gridManager.getyGridEnd();

        //UI positions
        buildButtonX = deviceWidth/2 - asset.BLANKICONPLACER.getWidth();
        buildButtonY = grid[grid.length-1][0].y + (gridManager.getTileWidth() + gridManager.getTileWidth()/2);
        removeButtonX = buildButtonX + asset.BUILD.getWidth() + gridManager.getTileWidth()/2;
        removeButtonY = grid[grid.length-1][0].y + (gridManager.getTileWidth() + gridManager.getTileWidth()/2);
        blankIconPlacerX = deviceWidth/2 - asset.BLANKICONPLACER.getWidth();
        blankIconPlacerY = buildButtonY + xGridStart + asset.BUILD.getHeight();
        towerDescriptorX = blankIconPlacerX + asset.BLANKICONPLACER.getWidth() + gridManager.getTileWidth()/2;
        towerDescriptorY = blankIconPlacerY;


        //Tower Icons
        towerIconOneX = blankIconPlacerX + 2;
        towerIconOneY = blankIconPlacerY + 2;
        towerIconTwoX  = towerIconOneX + 3 + towerIconWidth;
        towerIconTwoY = blankIconPlacerY + 2;
        towerIconThreeX = towerIconTwoX + 3 + towerIconWidth;
        towerIconThreeY  = blankIconPlacerY + 2;
        towerIconFourX = blankIconPlacerX + 2;
        towerIconFourY = towerIconOneY + 3 + towerIconWidth;
        towerIconFiveX = towerIconFourX + 3 + towerIconWidth;
        towerIconFiveY = towerIconOneY + 3 + towerIconWidth;
        towerIconSixX = towerIconFiveX + 3 + towerIconWidth;
        towerIconSixY = towerIconOneY + 3 + towerIconWidth;
        towerIconSevenX = blankIconPlacerX + 2;
        towerIconSevenY = towerIconFourY + 3 + towerIconWidth;
        towerIconEightX = towerIconSevenX + 3 + towerIconWidth;
        towerIconEightY  = towerIconFourY + 3 + towerIconWidth;
        towerIconNineX = towerIconEightX + 3 + towerIconWidth;
        towerIconNineY = towerIconFourY + 3 + towerIconWidth;
        currentTowerIconHighlightX = towerIconOneX;
        currentTowerIconHighlightY = towerIconOneY;


        //Higlight and placement managers
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

            //If user presses on a tower icon, make that the new selected icon (default is 1)
            ifTouchInTowerIcon(motionEvent);

            //Add tower
            if(ifTouchIsInBuildButton(motionEvent)){
                asset.buildPressed();
                if(gold >= getSelectedTowerCost()) {    //If enough gold todo: fix this
                    if (placementManager.checkSpotAvailability(highlightManager.getHighlightPlacement())) { //If spot available
                        towers.add(addSelectedTowerType());
                        for (int i = 0; i < wave.size(); i++) {
                            wave.get(i).pathNeedsUpdating = true;
                        }
                    }
                }
                else{
                    Toast.makeText(context,(String)"Not enough gold!", Toast.LENGTH_SHORT).show();
                }
            }

            //Remove tower
            if(ifTouchIsInRemoveButton(motionEvent)){
                //Check each tower if it's the one that's highlighted
                for (int i = 0; i < towers.size(); i++) {
                    if(towers.get(i).selectTower(highlightManager.getHighlightPlacement())){
                        placementManager.removeTower(highlightManager.getHighlightPlacement()); //Remove tower from grid
                        gold += Math.ceil(towers.get(i).getCost() * returnRate);
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
        drawHighLight(canvas);
        drawCurrentWave(canvas);
        drawTowerProjectiles(canvas);
        drawUI(canvas);
        drawInfoBarAndDescriptorStuff(canvas);


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
            towers.get(i).drawTower(canvas, asset);
        }
    }

    private void drawTowerProjectiles(Canvas canvas){
        for (int i = 0; i < towers.size(); i++) {
            towers.get(i).drawProjectile(canvas, asset);
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
        canvas.drawBitmap(asset.BUILD, buildButtonX, buildButtonY, null);
        //Remove button
        canvas.drawBitmap(asset.REMOVE, removeButtonX, removeButtonY, null);
        //BlankIconPlacer
        canvas.drawBitmap(asset.BLANKICONPLACER, blankIconPlacerX, blankIconPlacerY, null);
        //Icon Placer Highlight
        paint.setARGB(255, 70, 62, 62);
        canvas.drawRect(currentTowerIconHighlightX, currentTowerIconHighlightY, currentTowerIconHighlightX + asset.SNOWMANICON.getWidth(), currentTowerIconHighlightY + asset.SNOWMANICON.getHeight(), paint);
        //Tower Icon 1
        canvas.drawBitmap(asset.SNOWMANICON, towerIconOneX, towerIconOneY, null);
        //Tower Icon 2
        canvas.drawBitmap(asset.ARROWTOWERICON, towerIconTwoX, towerIconTwoY, null);
        //Tower Icon 3
        canvas.drawBitmap(asset.SNOWMANICON, towerIconThreeX, towerIconThreeY, null);
        //Tower Icon 4
        canvas.drawBitmap(asset.SNOWMANICON, towerIconFourX, towerIconFourY, null);
        //Tower Icon 5
        canvas.drawBitmap(asset.SNOWMANICON, towerIconFiveX, towerIconFiveY, null);
        //Tower Icon 6
        canvas.drawBitmap(asset.SNOWMANICON, towerIconSixX, towerIconSixY, null);
        //Tower Icon 7
        canvas.drawBitmap(asset.SNOWMANICON, towerIconSevenX, towerIconSevenY, null);
        //Tower Icon 8
        canvas.drawBitmap(asset.SNOWMANICON, towerIconEightX, towerIconEightY, null);
        //Tower Icon 9
        canvas.drawBitmap(asset.SNOWMANICON, towerIconNineX, towerIconNineY, null);

        //Descriptor
        canvas.drawBitmap(asset.TOWERDESCRIPTOR, towerDescriptorX, towerDescriptorY, null);


    }


    private void drawInfoBarAndDescriptorStuff(Canvas canvas){
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
        if(lives > 30){
            textPaint.setARGB(255, 0, 160, 0);
        }
        else if(lives > 10){
            textPaint.setARGB(255, 180, 180, 0);
        }
        else{
            textPaint.setARGB(255, 190, 0, 0);
        }
        staticLayout = new StaticLayout("Lives: " + lives, textPaint, 150, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        canvas.save();
        canvas.translate( deviceWidth*3/5 - staticLayout.getWidth()/2, deviceHeight/80 - textSize/2);
        staticLayout.draw(canvas);
        canvas.restore();

        //Set the text of the current gold
        textPaint.setARGB(255, 170, 160, 0);
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

        //Todo descriptor stuff here

        //invalidate();
    }

    private boolean ifTouchIsInBuildButton(MotionEvent motionEvent){
        return buildButtonX < motionEvent.getX() && motionEvent.getX() < buildButtonX + asset.BUILD.getWidth()
                && buildButtonY < motionEvent.getY() && motionEvent.getY() < buildButtonY + asset.BUILD.getHeight();
    }

    private boolean ifTouchIsInRemoveButton(MotionEvent motionEvent){
        return removeButtonX < motionEvent.getX() && motionEvent.getX() < removeButtonX + asset.REMOVE.getWidth()
                && removeButtonY < motionEvent.getY() && motionEvent.getY() < removeButtonY + asset.REMOVE.getHeight();
    }

    private void ifTouchInTowerIcon(MotionEvent motionEvent){
        //Select tower depending on icon selected
            if(towerIconOneX < motionEvent.getX() && motionEvent.getX() < towerIconOneX + towerIconWidth
                && towerIconOneY < motionEvent.getY() && motionEvent.getY() < towerIconOneY + towerIconWidth){
                currentTowerIconHighlightX = towerIconOneX;
                currentTowerIconHighlightY = towerIconOneY;
                selectedTowerIcon = 1;
            }
            else if(towerIconTwoX < motionEvent.getX() && motionEvent.getX() < towerIconTwoX + towerIconWidth
                    && towerIconTwoY < motionEvent.getY() && motionEvent.getY() < towerIconTwoY + towerIconWidth){
                currentTowerIconHighlightX = towerIconTwoX;
                currentTowerIconHighlightY = towerIconTwoY;
                selectedTowerIcon = 2;
            }
            else if(towerIconThreeX < motionEvent.getX() && motionEvent.getX() < towerIconThreeX + towerIconWidth
                    && towerIconThreeY < motionEvent.getY() && motionEvent.getY() < towerIconThreeY + towerIconWidth){
                currentTowerIconHighlightX = towerIconThreeX;
                currentTowerIconHighlightY = towerIconThreeY;
                selectedTowerIcon = 3;
            }
            else if(towerIconFourX < motionEvent.getX() && motionEvent.getX() < towerIconFourX + towerIconWidth
                    && towerIconFourY < motionEvent.getY() && motionEvent.getY() < towerIconFourY + towerIconWidth){
                currentTowerIconHighlightX = towerIconFourX;
                currentTowerIconHighlightY = towerIconFourY;
                selectedTowerIcon = 4;
            }
            else if(towerIconFiveX < motionEvent.getX() && motionEvent.getX() < towerIconFiveX + towerIconWidth
                    && towerIconFiveY < motionEvent.getY() && motionEvent.getY() < towerIconFiveY + towerIconWidth){
                currentTowerIconHighlightX = towerIconFiveX;
                currentTowerIconHighlightY = towerIconFiveY;
                selectedTowerIcon = 5;
            }
            else if(towerIconSixX < motionEvent.getX() && motionEvent.getX() < towerIconSixX + towerIconWidth
                    && towerIconSixY < motionEvent.getY() && motionEvent.getY() < towerIconSixY + towerIconWidth){
                currentTowerIconHighlightX = towerIconSixX;
                currentTowerIconHighlightY = towerIconSixY;
                selectedTowerIcon = 6;
            }
            else if(towerIconSevenX < motionEvent.getX() && motionEvent.getX() < towerIconSevenX + towerIconWidth
                    && towerIconSevenY < motionEvent.getY() && motionEvent.getY() < towerIconSevenY + towerIconWidth){
                currentTowerIconHighlightX = towerIconSevenX;
                currentTowerIconHighlightY = towerIconSevenY;
                selectedTowerIcon = 7;
            }
            else if(towerIconEightX < motionEvent.getX() && motionEvent.getX() < towerIconEightX + towerIconWidth
                    && towerIconEightY < motionEvent.getY() && motionEvent.getY() < towerIconEightY + towerIconWidth){
                currentTowerIconHighlightX = towerIconEightX;
                currentTowerIconHighlightY = towerIconEightY;
                selectedTowerIcon = 8;
            }
            else if(towerIconNineX < motionEvent.getX() && motionEvent.getX() < towerIconNineX + towerIconWidth
                    && towerIconNineY < motionEvent.getY() && motionEvent.getY() < towerIconNineY + towerIconWidth){
                currentTowerIconHighlightX = towerIconNineX;
                currentTowerIconHighlightY = towerIconNineY;
                selectedTowerIcon = 9;
            }
    }

    private Tower addSelectedTowerType(){
        switch (selectedTowerIcon){
            case 1:
                return new SnowballTower(highlightManager.getHighlightPlacement(), placementManager);
            case 2:
                return new ArrowTower(highlightManager.getHighlightPlacement(), placementManager);
            case 3:
                return new SnowballTower(highlightManager.getHighlightPlacement(), placementManager);
            case 4:
                return new SnowballTower(highlightManager.getHighlightPlacement(), placementManager);
            case 5:
                return new SnowballTower(highlightManager.getHighlightPlacement(), placementManager);
            case 6:
                return new SnowballTower(highlightManager.getHighlightPlacement(), placementManager);
            case 7:
                return new SnowballTower(highlightManager.getHighlightPlacement(), placementManager);
            case 8:
                return new SnowballTower(highlightManager.getHighlightPlacement(), placementManager);
            case 9:
                return new SnowballTower(highlightManager.getHighlightPlacement(), placementManager);
            default:
                return new SnowballTower(highlightManager.getHighlightPlacement(), placementManager);

        }
    }

    private int getSelectedTowerCost(){
        switch (selectedTowerIcon){
            case 1:
                return SnowballTower.cost;
            case 2:
                return ArrowTower.cost;
            case 3:
                return SnowballTower.cost;
            case 4:
                return SnowballTower.cost;
            case 5:
                return SnowballTower.cost;
            case 6:
                return SnowballTower.cost;
            case 7:
                return SnowballTower.cost;
            case 8:
                return SnowballTower.cost;
            case 9:
                return SnowballTower.cost;
            default:
                return SnowballTower.cost;

        }
    }

    private void initWaves(){
        wave = new ArrayList<>();

        //Add waves. These are how the levels are designed.
        wave.add(new Wave(asset, "ghost", 10, waveID++));
        wave.add(new Wave(asset, "sleddingelf",20, waveID++));
        wave.add(new Wave(asset, "eye", 5, waveID++));



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
                    gold += currentWave * 4;    //Give gold equal to the next wave times 3.
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