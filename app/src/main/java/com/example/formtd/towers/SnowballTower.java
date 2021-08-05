package com.example.formtd.towers;
import com.example.formtd.*;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;

import com.example.formtd.PlacementManager;
import com.example.formtd.RectanglePoints;

public class SnowballTower extends Tower {
    public SnowballTower(RectanglePoints rect, PlacementManager placementManager) {
        super(rect, placementManager);
    }

    public void drawTower(Canvas canvas){
        paint.setARGB(255, 255, 235, 255);
        canvas.drawRoundRect(left, top, right, bottom, 40, 20, paint);

    }
}
