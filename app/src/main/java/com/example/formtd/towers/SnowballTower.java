package com.example.formtd.towers;

import android.graphics.Canvas;
import com.example.formtd.PlacementManager;
import com.example.formtd.RectanglePoints;

public class SnowballTower extends Tower {
    public SnowballTower(RectanglePoints rect, PlacementManager placementManager) {
        super(rect, placementManager);
    }

    public void drawTower(Canvas canvas){
        //Draw tower underlay
        paint.setARGB(255, 140, 165, 245);
        canvas.drawRect(left, top, right, bottom, paint);

        //Draw actual tower
        paint.setARGB(255, 255, 235, 255);
        canvas.drawRoundRect(left, top, right, bottom, 55, 30, paint);

    }
}
