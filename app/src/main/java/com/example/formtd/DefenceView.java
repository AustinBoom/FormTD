package com.example.formtd;
import com.example.formtd.towers.*;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.os.HandlerCompat;
import java.util.ArrayList;


public class DefenceView extends View implements View.OnTouchListener {
    //Boiler Plate
    Context context;

    //World
    int deviceWidth;
    int deviceHeight;
    AssetManager asset;
    GridManager gridManager;                //Creates/updates grid
    HighlightManager highlightManager;      //Manages placement via touch.
    PlacementManager placementManager;              //Manages existing towers and spot availability.
    Grid[][] grid;                         //The grid which is used among different managers.
    ArrayList<Tower> towers;
    Paint paint;
    int y = 100;


    public DefenceView(Context context) {
        super(context);
        this.context = context;
        setOnTouchListener(this);
        asset = new AssetManager(context);
        towers = new ArrayList<>();
        paint = new Paint();
    }

    public DefenceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        setOnTouchListener(this);
        asset = new AssetManager(context);
        towers = new ArrayList<>();
        paint = new Paint();

    }


    //CRUCIAL measurement, this method initializes a GridManager
    protected void onMeasure(int widthMeasure, int heightMeasure){
        deviceWidth = MeasureSpec.getSize(widthMeasure);
        deviceHeight = MeasureSpec.getSize(heightMeasure);

        gridManager = new GridManager(deviceWidth, deviceHeight);
        grid = gridManager.getGrid();
        highlightManager = new HighlightManager(grid, gridManager.getTileWidth(), gridManager.getxMapStart(), gridManager.getyMapStart());
        placementManager = new PlacementManager(grid, gridManager.getTileWidth());

        //Boiler plate. Removing this is CATASTROPHIC!
        setMeasuredDimension(deviceWidth, deviceHeight);
    }

    

    //When screen is touched, respond!
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            //Log.i("\t", "x: " + motionEvent.getX() + " y: " + motionEvent.getY());
            highlightManager.setHighlightPlacement((int)motionEvent.getX(), (int)motionEvent.getY());

            if(ifTouchIsInBuildButton(motionEvent)){
                asset.buildPressed();
                if(placementManager.checkSpotAvailability(highlightManager.getHighlightPlacement())){
                    towers.add(new SnowballTower(highlightManager.getHighlightPlacement(), placementManager));
                }
            }
            grid = placementManager.updateGrid();
        }



        return true;       //false: don't listen for subsequent events (change to true for something like a double tap)
    }



    //Draw listener that updates the view. (NOTE: order of drawing matters!)
    @Override
    public void onDraw(Canvas canvas){
        //Just to test drawview
        gridManager.drawGrid(canvas);
        drawUI(canvas);
        drawTowers(canvas);
        drawTestBall(canvas);
        drawHighLight(canvas);


        //invalidate();       //CRITICAL FOR ANIMATION. This makes drawview continulously update
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

    private void drawUI(Canvas canvas){
        //Top bar
        paint.setARGB(255, 50, 70, 90);
        canvas.drawRect(0, 0, deviceWidth, deviceHeight/40, paint);

        //Build Button
        canvas.drawBitmap(asset.BUILD, deviceWidth - gridManager.getxMapStart() - (asset.BUILD.getWidth()), grid[grid.length-1][0].y + (gridManager.getTileWidth() + gridManager.getTileWidth()/2), null);
    }

    private boolean ifTouchIsInBuildButton(MotionEvent motionEvent){
        if(deviceWidth - gridManager.getxMapStart() - (asset.BUILD.getWidth()) < motionEvent.getX() && motionEvent.getX() <  deviceWidth - gridManager.getxMapStart()
                && grid[grid.length-1][0].y + (gridManager.getTileWidth() + gridManager.getTileWidth()/2) < motionEvent.getY() && motionEvent.getY() < grid[grid.length-1][0].y + (gridManager.getTileWidth() + gridManager.getTileWidth()/2) + asset.BUILD.getHeight()){
            return true;
        }
        return false;
    }

    private void drawTestBall(Canvas canvas){
        paint.setARGB(255, 255, 255, 0);
        canvas.drawCircle(100, y, 50, paint);


        final Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
        final Runnable runnable = new Runnable() {

            public void run() {
                invalidate();
                y += 1;
            }
        };
        handler.postDelayed(runnable, 0); //Start animation

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