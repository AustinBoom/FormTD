package com.example.formtd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class DefenceView extends View implements View.OnTouchListener {
    //Boiler Plate
    Context context;

    //World
    int deviceWidth;
    int deviceHeight;

    //Defence View
    GridManager gridManager;
    Point[][] grid;

    public DefenceView(Context context) {
        super(context);
        this.context = context;
    }

    public DefenceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
    }

    //CRUCIAL measurement, this method is called once and initializes a GridManager
    protected void onMeasure(int widthMeasure, int heightMeasure){
        deviceWidth = MeasureSpec.getSize(widthMeasure);
        deviceHeight = MeasureSpec.getSize(heightMeasure);

        gridManager = new GridManager(deviceWidth, deviceHeight);
        grid = gridManager.getGrid();

        //Boiler plate. Removing this is CATASTROPHIC!
        setMeasuredDimension(deviceWidth, deviceHeight);
    }



    //---------------------------------

    //When screen is touched, respond!
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        invalidate();

        return false;
    }

    //Draw listener that updates the view.
    public void onDraw(Canvas canvas){

        //Just to test drawview
        Paint paint = new Paint();
        paint.setARGB(255, 155, 180, 255);
        canvas.drawRect(deviceWidth/40, deviceHeight/40,
                deviceWidth*39/40, deviceHeight*3/4, paint);
        drawGrid(canvas);
    }

    //Draws the points represented by a grid using random colors. For testing purposes.
    private void drawGrid(Canvas canvas){
        //Print array
        Paint paint = new Paint();
        Random ran = new Random();
        int left;
        int top;
        int right;
        int bottom;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                left = grid[y][x].x;
                right = left + gridManager.getTileWidth();
                top = grid[y][x].y;
                bottom = top + gridManager.getTileWidth();
                paint.setARGB(255, ran.nextInt(255) , ran.nextInt(255), ran.nextInt(255));
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }
    }


}