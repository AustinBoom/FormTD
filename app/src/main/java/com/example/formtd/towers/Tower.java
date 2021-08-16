package com.example.formtd.towers;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.example.formtd.AssetManager;
import com.example.formtd.DefenceView;
import com.example.formtd.PlacementManager;
import com.example.formtd.RectanglePoints;
import com.example.formtd.enemies.Enemy;


//Abstract tower class, holds basic tower functions, specific tower will be created upon child instantiation.
public abstract class Tower {
    PlacementManager placementManager;
    Paint paint;
    private float angle;
    protected int left;
    protected int top;
    protected int right;
    protected int bottom;
    public int towerCenterX;
    public int towerCenterY;
    public int aggroEnemy;      //Index of aggro'd wave
    public int currentWaveID;        //Making sure the wave is in sync with aggro
    public boolean projecting;          //Mark whether or not to send projectile
    public boolean alreadyAttacking;    //Make sure only one instance of projectile is being triggered.
    private int projectileX;
    private int projectileY;

    //Customizables
    public int attackDamage = 1;       //Amount of damage tower does
    public int attackRange = 300;       //Radius of attack
    public int projectileSpeed = 6;    //Speed of projectile animation
    public int tolerance = 4;
    public int projectileRadius = 6;
    public final int cost = 0;


    public Tower(RectanglePoints rect, PlacementManager placementManager){
        this.placementManager = placementManager;
        placementManager.placeTower(rect);
        paint = new Paint();
        this.angle = 0;
        this.left = rect.left;
        this.top = rect.top;
        this.right = rect.right;
        this.bottom = rect.bottom;
        this.towerCenterX = (left + right)/2;
        this.towerCenterY = (top + bottom)/2;
        this.projecting = false;
        this.alreadyAttacking = false;
        this.projectileX = towerCenterX;
        this.projectileY = towerCenterY;
        this.aggroEnemy = -1;

        //Upon creation charge gold
        DefenceView.gold -= cost;
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

    public int getCost(){
        return this.cost;
    }

    //Draws towers and initializes canvas.
    public abstract void drawTower(Canvas canvas, AssetManager asset);

    public abstract void drawProjectile(Canvas canvas, AssetManager asset);

    //Constantly called by DefenceView towerHandler. Look for nearby enemies and attack!
    public abstract Enemy[] watch(Enemy[] enemies, int waveID);

    public abstract Enemy updateProjectile(Enemy enemy);

}
