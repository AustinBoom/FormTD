package com.example.formtd;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class Tower {
    PlacementManager placementManager;
    private int left;
    private int top;
    private int right;
    private int bottom;

    public Tower(RectanglePoints rect, PlacementManager placementManager){
        this.placementManager = placementManager;
        placementManager.placeTower(rect);
        this.left = rect.left;
        this.top = rect.top;
        this.right = rect.right;
        this.bottom = rect.bottom;

    }

    public void drawTower(Canvas canvas){
        Paint paint = new Paint();
        paint.setARGB(255, 255, 0, 255);
        canvas.drawRect(left, top, right, bottom, paint);
    }

}
