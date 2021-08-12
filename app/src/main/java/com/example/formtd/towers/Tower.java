package com.example.formtd.towers;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.formtd.PlacementManager;
import com.example.formtd.RectanglePoints;
import com.example.formtd.enemies.Enemy;


//Abstract tower class, holds basic tower functions, specific tower will be created upon child instantiation.
public abstract class Tower {
    PlacementManager placementManager;
    Paint paint;
    Canvas canvas;      //Initialized by drawTower
    protected int left;
    protected int top;
    protected int right;
    protected int bottom;
    public Enemy aggroEnemy;
    public int attackDamage = 10;
    public int attackRange = 500;
    public int attackSpeed = 100;       //Time between attacks
    public int projectileSpeed = 30;    //Speed of projectile animation


    public Tower(RectanglePoints rect, PlacementManager placementManager){
        this.placementManager = placementManager;
        placementManager.placeTower(rect);
        paint = new Paint();
        this.left = rect.left;
        this.top = rect.top;
        this.right = rect.right;
        this.bottom = rect.bottom;
    }

    //If points all match up, this is the tower selected
    public boolean selectTower(RectanglePoints rectanglePoints){
        int rectleft = rectanglePoints.left;
        int recttop = rectanglePoints.top;
        int rectright = rectanglePoints.right;
        int rectbottom = rectanglePoints.bottom;

        if(left == rectleft && top == recttop && right == rectright && bottom == rectbottom){
            return true;
        }
        return false;
    }

    //Draws towers and initializes canvas.
    public void drawTower(Canvas canvas){
        paint.setARGB(255, 255, 0, 255);
        canvas.drawRect(left, top, right, bottom, paint);
        this.canvas = canvas;
    }

}
