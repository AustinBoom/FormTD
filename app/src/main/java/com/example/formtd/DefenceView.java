package com.example.formtd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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

    public DefenceView(Context context) {
        super(context);
        this.context = context;
        setOnTouchListener(this);
        asset = new AssetManager(context);
        towers = new ArrayList<>();
    }

    public DefenceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        setOnTouchListener(this);
        asset = new AssetManager(context);
        towers = new ArrayList<>();
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



    //---------------------------------

    //When screen is touched, respond!
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            //Log.i("\t", "x: " + motionEvent.getX() + " y: " + motionEvent.getY());
            highlightManager.setHighlightPlacement((int)motionEvent.getX(), (int)motionEvent.getY());

            if(ifTouchIsInBuildButton(motionEvent)){
                System.out.println("CLICKEDDDDDDDDDDDDDDDDDDDDDD");
                if(placementManager.checkSpotAvailability(highlightManager.getHighlightPlacement())){
                    towers.add(new Tower(highlightManager.getHighlightPlacement()));
                }
            }
            invalidate(); //Boiler plate
        }
        return true;       //false: don't listen for subsequent events (change to true for something like a double tap)
    }


    //Draw listener that updates the view.
    @Override
    public void onDraw(Canvas canvas){
        //Just to test drawview
        Paint paint = new Paint();
        paint.setARGB(255, 50, 70, 90);
        canvas.drawRect(0, 0, deviceWidth, deviceHeight/40, paint);
        gridManager.drawGrid(canvas);
        drawUI(canvas);
        drawTowers(canvas);
        drawHighLight(canvas);

    }


    //Draw the highlight from the point tapped on the map
    private void drawHighLight(Canvas canvas){
        //Get the highlighted points. If null then there is no highlight to draw; simply do nothing.
        RectanglePoints rectanglePoints = highlightManager.getHighlightPlacement();
        if(rectanglePoints != null){
            Paint paint = new Paint();
            if(placementManager.checkSpotAvailability(rectanglePoints.left, rectanglePoints.top))
                paint.setARGB(80, 50, 255, 50);
            else
                paint.setARGB(80, 255, 50, 50);

            canvas.drawRect(rectanglePoints.left, rectanglePoints.top, rectanglePoints.right, rectanglePoints.bottom, paint);
        }
    }

    private void drawTowers(Canvas canvas){
        for (int i = 0; i < towers.size(); i++) {
            towers.get(i).build(canvas);
        }
    }

    private void drawUI(Canvas canvas){
        canvas.drawBitmap(asset.BUILD, deviceWidth - gridManager.getxMapStart() - (asset.BUILD.getWidth()), grid[grid.length-1][0].y + (gridManager.getTileWidth() + gridManager.getTileWidth()/2), null);
    }

    private boolean ifTouchIsInBuildButton(MotionEvent motionEvent){
        if(deviceWidth - gridManager.getxMapStart() - (asset.BUILD.getWidth()) < motionEvent.getX() && motionEvent.getX() <  deviceWidth - gridManager.getxMapStart()
                && grid[grid.length-1][0].y + (gridManager.getTileWidth() + gridManager.getTileWidth()/2) < motionEvent.getY() && motionEvent.getY() < grid[grid.length-1][0].y + (gridManager.getTileWidth() + gridManager.getTileWidth()/2) + asset.BUILD.getHeight()){
            return true;
        }
        return false;
    }


}