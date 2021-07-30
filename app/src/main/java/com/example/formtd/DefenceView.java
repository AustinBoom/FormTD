package com.example.formtd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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
    Grid[][] grid;                         //The grid which is used among different managers.

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
    @Override
    public void onDraw(Canvas canvas){
        //Just to test drawview
        Paint paint = new Paint();
        paint.setARGB(255, 50, 70, 90);
        canvas.drawRect(0, 0, deviceWidth, deviceHeight/40, paint);
        gridManager.drawGrid(canvas);
        drawHighLight(canvas);
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