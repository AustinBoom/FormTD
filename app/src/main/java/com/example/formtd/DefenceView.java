/**
 * @author Austin Huyboom.
 *
 */

package com.example.formtd;
import com.example.formtd.difficulty.*;
import com.example.formtd.towers.*;

import android.content.Context;
import android.graphics.Bitmap;
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
    AssetManager asset;
    GridManager gridManager;                        //Creates/updates grid
    HighlightManager highlightManager;              //Manages placement via touch.
    PlacementManager placementManager;              //Manages existing towers and spot availability.
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
    public int enemyDescriptorX;
    public int enemyDescriptorY;
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
    public int easySelectorX;
    public int mediumSelectorX;
    public int hardSelectorX;
    public int difficultySelectorY;
    public static final int towerIconWidth = 146;   //To fit inside box.
    public String towerDescriptorDescription;
    public int towerDescriptorCost;
    public int towerDescriptorDamage;
    public int towerDescriptorRange;
    public String towerDescriptorSpeed;
    public String towerDescriptorAccuracy;
    public Bitmap currentWaveBitmap;
    public int currentWaveHealth;

    public static ArrayList<Tower> towers;          //Towers are created/removed here, and managed in Wave class.
    public static boolean pathNeedsUpdating = false;    //Whenever a tower is placed, set this to true and Wave class will know about it.
    Paint paint;

    //Mechanics
    StaticLayout staticLayout;      //For text
    TextPaint textPaint = new TextPaint();
    final int textSize = 32;
    ArrayList<Wave> wave;            //Holds every wave that exists
    public boolean begin = false;   //When game has begun
    protected int waveTimer = 70000;           //Time between waves (ex. 60000ms = 60 seconds)
    protected int firstWaveReduction = 40000;   //Make the first wave shorter
    protected int countdown = 0;              //Countdown timer. Set to waveTimer/1000 then counts down each wave. (do not set here)
    public static boolean gameOver = false;
    public static boolean lastWave = false;
    public static int currentWave = 0;
    public static int lives = 50;
    public static int gold = 100;
    public static float returnRate = 0.8f;
    public static boolean blocking = false;
    public static int waveID = 0;       //Global id assigned to enemies. Used to count up for uniqueness. (don't need heavy duty like UUID)
    public int selectedTowerIcon = 1;
    public static Difficulty difficultyModifier;


    public DefenceView(Context context) {
        super(context);
        this.context = context;
        setOnTouchListener(this);
        towers = new ArrayList<>();
        paint = new Paint();
        difficultyModifier = new Medium();
    }

    public DefenceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        setOnTouchListener(this);
        towers = new ArrayList<>();
        paint = new Paint();
        difficultyModifier = new Medium();
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
        enemyDescriptorX = blankIconPlacerX + asset.BLANKICONPLACER.getWidth() + gridManager.getTileWidth()/2;
        enemyDescriptorY = buildButtonY;
        currentWaveBitmap = asset.DEFAULTBITMAP;
        currentWaveHealth = 0;

        //Tower descriptions. Start out with snowball tower as default.
        towerDescriptorDescription = "This tower is more of a wall than anything...";
        towerDescriptorCost  = SnowballTower.cost;
        towerDescriptorDamage = SnowballTower.attackDamage;
        towerDescriptorRange = SnowballTower.attackRange;
        towerDescriptorSpeed = "0";
        towerDescriptorAccuracy = "0";


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

        //Difficulty selectors
        difficultySelectorY = deviceHeight/3 - asset.EASY.getHeight()/2;
        mediumSelectorX = deviceWidth/2 - asset.MEDIUM.getWidth()/2;
        easySelectorX = mediumSelectorX - asset.MEDIUM.getWidth()/2 - asset.EASY.getWidth()/2 - tileWidth;
        hardSelectorX = mediumSelectorX + asset.MEDIUM.getWidth()/2 + asset.HARD.getWidth()/2 + tileWidth;


        //Higlight and placement managers
        highlightManager = new HighlightManager(grid, gridManager.getTileWidth(), gridManager.getxGridStart(), gridManager.getyGridStart());
        placementManager = new PlacementManager(grid, gridManager.getTileWidth());

        //Boiler plate. Removing this is CATASTROPHIC!
        setMeasuredDimension(deviceWidth, deviceHeight);
    }

    

    //When screen is touched, respond!
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP && lives > 0) {
            if(!begin){     //ONE TIME BEGINNING STUFF HERE. Once screen is touched start certain actions.
                selectDifficulty(motionEvent);
            }
            else {
                //Update highlight
                highlightManager.setHighlightPlacement((int) motionEvent.getX(), (int) motionEvent.getY());
                //If user presses on a tower icon, make that the new selected icon (default is 1)
                ifTouchInTowerIcon(motionEvent);

                //Add tower
                if (ifTouchIsInBuildButton(motionEvent)) {
                    asset.buildPressed();
                    if (gold >= getSelectedTowerCost()) {    //If enough gold todo: fix this
                        if (placementManager.checkSpotAvailability(highlightManager.getHighlightPlacement())) { //If spot available
                            towers.add(addSelectedTowerType());
                            for (int i = 0; i < wave.size(); i++) {
                                wave.get(i).pathNeedsUpdating = true;
                            }
                        }
                    } else {
                        Toast.makeText(context, (String) "Not enough gold!", Toast.LENGTH_SHORT).show();
                    }
                }

                //Remove tower
                if (ifTouchIsInRemoveButton(motionEvent)) {
                    //Check each tower if it's the one that's highlighted
                    for (int i = 0; i < towers.size(); i++) {
                        if (towers.get(i).selectTower(highlightManager.getHighlightPlacement())) {
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

        if(lives <= 0){
            defeatText(canvas);
        }
        //invalidate();       //PUT SOMEWHERE ELSE. This makes drawview update
    }

    private void selectDifficulty(MotionEvent motionEvent){
        //Touch is within Y range,
        if(motionEvent.getY() > difficultySelectorY && motionEvent.getY() < difficultySelectorY + asset.EASY.getHeight()){
            //Easy
            if(motionEvent.getX() > easySelectorX && motionEvent.getX() < easySelectorX + asset.EASY.getWidth()){
                difficultyModifier = new Easy();
                begin = true;
            }//Medium
            else if(motionEvent.getX() > mediumSelectorX && motionEvent.getX() < mediumSelectorX + asset.EASY.getWidth()){
                difficultyModifier = new Medium();
                begin = true;

            }//Hard
            else if(motionEvent.getX() > hardSelectorX && motionEvent.getX() < hardSelectorX + asset.EASY.getWidth()){
                difficultyModifier = new Hard();
                begin = true;
            }
        }
        //If difficulty selected, begin. (this happens only once)
        if(begin){
            startWaves();
            lives += difficultyModifier.getLivesModifier();
            returnRate += difficultyModifier.getSellValueModifier();
            gold += difficultyModifier.getStartGoldModifier();
            initWaves();        //Set up waves now that settings are in place.
        }
    }

    private void defeatText(Canvas canvas){
        textPaint.setARGB(150, 255, 10, 10);
        textPaint.setTextSize(textSize*3);
        textPaint.setFakeBoldText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        staticLayout = new StaticLayout("DEFEAT: GAME OVER", textPaint, deviceWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        canvas.save();
        canvas.translate( deviceWidth/2, deviceHeight / 6 - (staticLayout.getHeight()/2));
        staticLayout.draw(canvas);
        canvas.restore();
        textPaint.setFakeBoldText(false);
        textPaint.setTextAlign(Paint.Align.LEFT);
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
            paint.setARGB(210, 255, 255, 255);
            canvas.drawBitmap(asset.TAPTOSTART, deviceWidth / 2 - (asset.TAPTOSTART.getWidth() / 2), deviceHeight / 6 - (asset.TAPTOSTART.getHeight() / 2), paint);

            //Display game mode selectors
            paint.setARGB(255, 255, 255, 255);
            //Easy
            canvas.drawBitmap(asset.EASY, easySelectorX, difficultySelectorY, paint);
            //Medium
            canvas.drawBitmap(asset.MEDIUM, mediumSelectorX, difficultySelectorY, paint);
            //Hard
            canvas.drawBitmap(asset.HARD, hardSelectorX, difficultySelectorY, paint);

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
        canvas.drawRect(currentTowerIconHighlightX, currentTowerIconHighlightY, currentTowerIconHighlightX + asset.SNOWMANTOWERICON.getWidth(), currentTowerIconHighlightY + asset.SNOWMANTOWERICON.getHeight(), paint);
        //Tower Icon 1
        canvas.drawBitmap(asset.SNOWMANTOWERICON, towerIconOneX, towerIconOneY, null);
        //Tower Icon 2
        canvas.drawBitmap(asset.ARROWTOWERICON, towerIconTwoX, towerIconTwoY, null);
        //Tower Icon 3
        canvas.drawBitmap(asset.FISHSPYICON, towerIconThreeX, towerIconThreeY, null);
        //Tower Icon 4
        canvas.drawBitmap(asset.FRUITSTANDTOWERICON, towerIconFourX, towerIconFourY, null);
        //Tower Icon 5
        canvas.drawBitmap(asset.GOLEMTOWERICON, towerIconFiveX, towerIconFiveY, null);
        //Tower Icon 6
        canvas.drawBitmap(asset.CASTLETOWERICON, towerIconSixX, towerIconSixY, null);
        //Tower Icon 7
        canvas.drawBitmap(asset.BULBTOWERICON, towerIconSevenX, towerIconSevenY, null);
        //Tower Icon 8
        canvas.drawBitmap(asset.WATERTOWERICON, towerIconEightX, towerIconEightY, null);
        //Tower Icon 9
        canvas.drawBitmap(asset.PANSYTOWERICON, towerIconNineX, towerIconNineY, null);

        //Descriptor
        canvas.drawBitmap(asset.TOWERDESCRIPTOR, towerDescriptorX, towerDescriptorY, null);

        //Enemy Descriptor
        canvas.drawBitmap(asset.ENEMYDESCRIPTOR, enemyDescriptorX, enemyDescriptorY, null);


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

        /**Descriptor information**/
        //Description
        textPaint.setARGB(255, 200, 200, 200);
        textPaint.setFakeBoldText(true);
        staticLayout = new StaticLayout( towerDescriptorDescription, textPaint, 400, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        canvas.save();
        canvas.translate( towerDescriptorX + 24, towerDescriptorY + 24 - textSize/2);
        staticLayout.draw(canvas);
        canvas.restore();
        //Cost
        staticLayout = new StaticLayout("Cost: " + towerDescriptorCost, textPaint, 400, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        canvas.save();
        canvas.translate( towerDescriptorX + 24, towerDescriptorY + 174 - textSize/2);
        staticLayout.draw(canvas);
        canvas.restore();
        //Damage
        staticLayout = new StaticLayout("Damage: " + towerDescriptorDamage, textPaint, 400, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        canvas.save();
        canvas.translate( towerDescriptorX + 24, towerDescriptorY + 224 - textSize/2);
        staticLayout.draw(canvas);
        canvas.restore();
        //Range
        staticLayout = new StaticLayout("Range: " + towerDescriptorRange, textPaint, 400, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        canvas.save();
        canvas.translate( towerDescriptorX + 24, towerDescriptorY + 274 - textSize/2);
        staticLayout.draw(canvas);
        canvas.restore();
        //Speed
        staticLayout = new StaticLayout("Speed: " + towerDescriptorSpeed, textPaint, 400, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        canvas.save();
        canvas.translate( towerDescriptorX + 24, towerDescriptorY + 324 - textSize/2);
        staticLayout.draw(canvas);
        canvas.restore();
        //Accuracy
        staticLayout = new StaticLayout("Accuracy: " + towerDescriptorAccuracy, textPaint, 400, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        canvas.save();
        canvas.translate( towerDescriptorX + 24, towerDescriptorY + 374 - textSize/2);
        staticLayout.draw(canvas);
        canvas.restore();
        textPaint.setFakeBoldText(false);

        /**Enemy descriptor **/
        canvas.drawBitmap(currentWaveBitmap, enemyDescriptorX + 12, enemyDescriptorY + 2, null);
        textPaint.setARGB(255, 200, 200, 200);
        staticLayout = new StaticLayout("Health rating: " + currentWaveHealth, textPaint, 400, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        canvas.save();
        canvas.translate( enemyDescriptorX + asset.ENEMYDESCRIPTOR.getWidth()/4, enemyDescriptorY - textSize/2 + asset.ENEMYDESCRIPTOR.getHeight()/2);
        staticLayout.draw(canvas);
        canvas.restore();

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
                towerDescriptorDescription = "This tower is more of a wall than anything...";
                towerDescriptorCost  = SnowballTower.cost;
                towerDescriptorDamage = SnowballTower.attackDamage;
                towerDescriptorRange = SnowballTower.attackRange;
                towerDescriptorSpeed = "0";
                towerDescriptorAccuracy = "0";
                selectedTowerIcon = 1;
            }
            else if(towerIconTwoX < motionEvent.getX() && motionEvent.getX() < towerIconTwoX + towerIconWidth
                    && towerIconTwoY < motionEvent.getY() && motionEvent.getY() < towerIconTwoY + towerIconWidth){
                currentTowerIconHighlightX = towerIconTwoX;
                currentTowerIconHighlightY = towerIconTwoY;
                towerDescriptorDescription = "Well rounded but not well versed.";
                towerDescriptorCost  = ArrowTower.cost;
                towerDescriptorDamage = ArrowTower.attackDamage;
                towerDescriptorRange = ArrowTower.attackRange;
                towerDescriptorSpeed = "Medium";
                towerDescriptorAccuracy = "Medium";
                selectedTowerIcon = 2;
            }
            else if(towerIconThreeX < motionEvent.getX() && motionEvent.getX() < towerIconThreeX + towerIconWidth
                    && towerIconThreeY < motionEvent.getY() && motionEvent.getY() < towerIconThreeY + towerIconWidth){
                currentTowerIconHighlightX = towerIconThreeX;
                currentTowerIconHighlightY = towerIconThreeY;
                towerDescriptorDescription = "Melee range but fairly vicious. Not really sure if you can trust it though.";
                towerDescriptorCost  = FishSpyTower.cost;
                towerDescriptorDamage = FishSpyTower.attackDamage;
                towerDescriptorRange = FishSpyTower.attackRange;
                towerDescriptorSpeed = "Fast";
                towerDescriptorAccuracy = "Good";
                selectedTowerIcon = 3;
            }
            else if(towerIconFourX < motionEvent.getX() && motionEvent.getX() < towerIconFourX + towerIconWidth
                    && towerIconFourY < motionEvent.getY() && motionEvent.getY() < towerIconFourY + towerIconWidth){
                currentTowerIconHighlightX = towerIconFourX;
                currentTowerIconHighlightY = towerIconFourY;
                towerDescriptorDescription = "Throw all the fruit! Decent range.";
                towerDescriptorCost  = FruitStandTower.cost;
                towerDescriptorDamage = FruitStandTower.attackDamage;
                towerDescriptorRange = FruitStandTower.attackRange;
                towerDescriptorSpeed = "Fast";
                towerDescriptorAccuracy = "Medium";
                selectedTowerIcon = 4;
            }
            else if(towerIconFiveX < motionEvent.getX() && motionEvent.getX() < towerIconFiveX + towerIconWidth
                    && towerIconFiveY < motionEvent.getY() && motionEvent.getY() < towerIconFiveY + towerIconWidth){
                currentTowerIconHighlightX = towerIconFiveX;
                currentTowerIconHighlightY = towerIconFiveY;
                towerDescriptorDescription = "Not very nimble, but being hit by a boulder hurts.";
                towerDescriptorCost  = GolemTower.cost;
                towerDescriptorDamage = GolemTower.attackDamage;
                towerDescriptorRange = GolemTower.attackRange;
                towerDescriptorSpeed = "Slow";
                towerDescriptorAccuracy = "Medium";
                selectedTowerIcon = 5;
            }
            else if(towerIconSixX < motionEvent.getX() && motionEvent.getX() < towerIconSixX + towerIconWidth
                    && towerIconSixY < motionEvent.getY() && motionEvent.getY() < towerIconSixY + towerIconWidth){
                currentTowerIconHighlightX = towerIconSixX;
                currentTowerIconHighlightY = towerIconSixY;
                towerDescriptorDescription = "Can't go wrong with sending out the troops. The range is huge!";
                towerDescriptorCost  = CastleTower.cost;
                towerDescriptorDamage = CastleTower.attackDamage;
                towerDescriptorRange = CastleTower.attackRange;
                towerDescriptorSpeed = "Fast";
                towerDescriptorAccuracy = "Good";
                selectedTowerIcon = 6;
            }
            else if(towerIconSevenX < motionEvent.getX() && motionEvent.getX() < towerIconSevenX + towerIconWidth
                    && towerIconSevenY < motionEvent.getY() && motionEvent.getY() < towerIconSevenY + towerIconWidth){
                currentTowerIconHighlightX = towerIconSevenX;
                currentTowerIconHighlightY = towerIconSevenY;
                towerDescriptorDescription = "An overloaded lightbulb is really dangerous.";
                towerDescriptorCost  = BulbTower.cost;
                towerDescriptorDamage = BulbTower.attackDamage;
                towerDescriptorRange = BulbTower.attackRange;
                towerDescriptorSpeed = "Extreme";
                towerDescriptorAccuracy = "Very good";
                selectedTowerIcon = 7;
            }
            else if(towerIconEightX < motionEvent.getX() && motionEvent.getX() < towerIconEightX + towerIconWidth
                    && towerIconEightY < motionEvent.getY() && motionEvent.getY() < towerIconEightY + towerIconWidth){
                currentTowerIconHighlightX = towerIconEightX;
                currentTowerIconHighlightY = towerIconEightY;
                towerDescriptorDescription = "Being slapped with water at high speed.";
                towerDescriptorCost  = WaterTower.cost;
                towerDescriptorDamage = WaterTower.attackDamage;
                towerDescriptorRange = WaterTower.attackRange;
                towerDescriptorSpeed = "Very Fast";
                towerDescriptorAccuracy = "Great";
                selectedTowerIcon = 8;
            }
            else if(towerIconNineX < motionEvent.getX() && motionEvent.getX() < towerIconNineX + towerIconWidth
                    && towerIconNineY < motionEvent.getY() && motionEvent.getY() < towerIconNineY + towerIconWidth){
                currentTowerIconHighlightX = towerIconNineX;
                currentTowerIconHighlightY = towerIconNineY;
                towerDescriptorDescription = "Why do they use \"pansy\" as an insult? Pansies are tough!";
                towerDescriptorCost  = PansyTower.cost;
                towerDescriptorDamage = PansyTower.attackDamage;
                towerDescriptorRange = PansyTower.attackRange;
                towerDescriptorSpeed = "Fast";
                towerDescriptorAccuracy = "Great";
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
                return new FishSpyTower(highlightManager.getHighlightPlacement(), placementManager);
            case 4:
                return new FruitStandTower(highlightManager.getHighlightPlacement(), placementManager, asset);
            case 5:
                return new GolemTower(highlightManager.getHighlightPlacement(), placementManager);
            case 6:
                return new CastleTower(highlightManager.getHighlightPlacement(), placementManager);
            case 7:
                return new BulbTower(highlightManager.getHighlightPlacement(), placementManager);
            case 8:
                return new WaterTower(highlightManager.getHighlightPlacement(), placementManager);
            case 9:
                return new PansyTower(highlightManager.getHighlightPlacement(), placementManager);
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
                return FishSpyTower.cost;
            case 4:
                return FruitStandTower.cost;
            case 5:
                return GolemTower.cost;
            case 6:
                return CastleTower.cost;
            case 7:
                return BulbTower.cost;
            case 8:
                return WaterTower.cost;
            case 9:
                return PansyTower.cost;
            default:
                return SnowballTower.cost;

        }
    }

    private void initWaves(){
        wave = new ArrayList<>();

        //Add waves. These are how the levels are designed.
        wave.add(new Wave(asset, "ant", 20, waveID++));
        wave.add(new Wave(asset, "ghost", 10, waveID++));
        wave.add(new Wave(asset, "sleddingelf",15, waveID++));
        wave.add(new Wave(asset, "waterghost", 10, waveID++));
        wave.add(new Wave(asset, "head", 15, waveID++));
        wave.add(new Wave(asset, "eye", 5, waveID++));
        wave.add(new Wave(asset, "lavaghost", 8, waveID++));
        wave.add(new Wave(asset, "snek", 15, waveID++));
        wave.add(new Wave(asset, "babyfishspy", 11, waveID++));
        wave.add(new Wave(asset, "milkglass", 9, waveID++));
        wave.add(new Wave(asset, "butterfly", 13, waveID++));
        wave.add(new Wave(asset, "crimsoneye", 3, waveID++));

    }


    //Updates the current wave when the time has come. If waves run out, game is over.
    private void startWaves(){
        final Timer timer = new Timer();
        TimerTask timerTask =  new TimerTask(){
            @Override
            public void run(){
                firstWaveReduction = 0;
                if(currentWave < wave.size() && !gameOver) {
                    if(currentWave == wave.size()-1)
                        lastWave = true;
                    wave.get(currentWave).startWave();
                    wave.get(currentWave).active = true;
                    currentWaveBitmap = wave.get(currentWave).enemies[0].art;
                    currentWaveHealth = wave.get(currentWave).enemies[0].health;
                    currentWave++;
                    gold += currentWave * (5 + difficultyModifier.getWaveGoldModifier());    //Give gold equal to the next wave times 3.
                }
                else{
                    //TODO put end of game stuff here
                    gameOver = true;
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, waveTimer-firstWaveReduction, waveTimer);  //Start wave
        startWaveTimer();                                           //Start wave timer
    }



    private void drawCurrentWave(Canvas canvas){
        if(begin) {
            for (Wave wave : wave) {     //for each wave in wave,
                if (wave.active) {
                    wave.drawWave(canvas);
                    invalidate();
                }
            }
        }
    }

    private void startWaveTimer(){
        countdown = (waveTimer-firstWaveReduction)/1000;
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