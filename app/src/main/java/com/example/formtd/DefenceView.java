package com.example.formtd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class DefenceView extends View implements View.OnTouchListener {
    //Boiler Plate
    Context context;

    //World
    int deviceWidth;
    int deviceHeight;

    //Defence View
    GridManager gridManager;                //Creates/updates grid
    PlacementManager placementManager;      //Manages placement via touch.
    TowerManager towerManager;              //Manages existing towers and spot availability.
    Point[][] grid;                         //The grid which is used among different managers.

    public DefenceView(Context context) {
        super(context);
        this.context = context;
        setOnTouchListener(this);
    }

    public DefenceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        setOnTouchListener(this);
    }

    //CRUCIAL measurement, this method initializes a GridManager
    protected void onMeasure(int widthMeasure, int heightMeasure){
        deviceWidth = MeasureSpec.getSize(widthMeasure);
        deviceHeight = MeasureSpec.getSize(heightMeasure);

        gridManager = new GridManager(deviceWidth, deviceHeight);
        grid = gridManager.getGrid();
        placementManager = new PlacementManager(grid, gridManager.getTileWidth(), gridManager.getxMapStart(), gridManager.getyMapStart());
        towerManager = new TowerManager(grid, gridManager.getTileWidth());

        //Boiler plate. Removing this is CATASTROPHIC!
        setMeasuredDimension(deviceWidth, deviceHeight);
    }



    //---------------------------------

    //When screen is touched, respond!
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            //Log.i("\t", "x: " + motionEvent.getX() + " y: " + motionEvent.getY());
            placementManager.setHighlightPlacement((int)motionEvent.getX(), (int)motionEvent.getY());
            invalidate(); //Boiler plate
        }
        return true;       //false: don't listen for subsequent events (change to true for something like a double tap)
    }

    //Draw listener that updates the view.
    public void onDraw(Canvas canvas){
        //Just to test drawview
        Paint paint = new Paint();
        paint.setARGB(255, 50, 70, 90);
        canvas.drawRect(0, 0, deviceWidth, deviceHeight/40, paint);
        gridManager.drawGrid(canvas);
        drawSpawnMidEndPoints(canvas);
        drawHighLight(canvas);
    }

    //Draws the spawn point, midpoint, and endpoint. This is hard coded since it's part of the world.
    private void drawSpawnMidEndPoints(Canvas canvas){
        Paint paint = new Paint();
        paint.setARGB(255, 65, 5, 35);
        //Draw spawnpoint
        RectanglePoints corner = new RectanglePoints(grid[0][0].x, grid[0][0].y, grid[0][grid[0].length-1].x + gridManager.getTileWidth(), grid[1][0].y);
        canvas.drawRoundRect(corner.left, corner.top, corner.right, corner.bottom, 20, 20, paint);
        //Draw endpoint
        corner.top = grid[grid.length-1][0].y;
        corner.bottom = grid[grid.length-1][0].y + gridManager.getTileWidth();
        canvas.drawRoundRect(corner.left, corner.top, corner.right, corner.bottom, 20, 20, paint);
        //Draw midpoint
        corner.left = grid[grid.length/2][grid[0].length/2 - 1].x;
        corner.top = grid[grid.length/2 - 1][grid[0].length/2].y;
        corner.right = corner.left + gridManager.getTileWidth() * 2;
        corner.bottom = corner.top + gridManager.getTileWidth() * 2;
        canvas.drawRoundRect(corner.left, corner.top, corner.right, corner.bottom, 20, 20, paint);


    }



    //Draw the highlight from the point tapped on the map
    private void drawHighLight(Canvas canvas){
        //Get the highlighted points. If null then there is no highlight to draw; simply do nothing.
        RectanglePoints rectanglePoints = placementManager.getHighlightPlacement();
        if(rectanglePoints != null){
            Paint paint = new Paint();
            if(towerManager.checkSpotAvailability(rectanglePoints.left, rectanglePoints.top))
                paint.setARGB(100, 50, 255, 50);
            else
                paint.setARGB(100, 255, 50, 50);

            canvas.drawRect(rectanglePoints.left, rectanglePoints.top,
                    rectanglePoints.right, rectanglePoints.bottom, paint);
        }

    }


}