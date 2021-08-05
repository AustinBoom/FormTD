package com.example.formtd.towers;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.formtd.PlacementManager;
import com.example.formtd.RectanglePoints;


//Abstract tower class, holds basic tower functions, specific tower will be created upon child instantiation.
public abstract class Tower {
    PlacementManager placementManager;
    Paint paint;
    protected int left;
    protected int top;
    protected int right;
    protected int bottom;

    public Tower(RectanglePoints rect, PlacementManager placementManager){
        this.placementManager = placementManager;
        placementManager.placeTower(rect);
        paint = new Paint();
        this.left = rect.left;
        this.top = rect.top;
        this.right = rect.right;
        this.bottom = rect.bottom;

    }

    public void drawTower(Canvas canvas){
        paint.setARGB(255, 255, 0, 255);
        canvas.drawRect(left, top, right, bottom, paint);
    }

}
