package com.example.formtd.towers;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.formtd.AssetManager;
import com.example.formtd.DefenceView;
import com.example.formtd.PlacementManager;
import com.example.formtd.RectanglePoints;
import com.example.formtd.enemies.Enemy;

public class SnowballTower extends Tower {
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
    public int attackDelayCounter;

    //Customizables
    public static int attackDamage = 0;       //Amount of damage tower does
    public static int attackRange = 0;       //Radius of attack
    public static int projectileSpeed = 0;    //Speed of projectile animation
    public static int tolerance = 0;           //Multiplies by projectile Radius
    public static int projectileRadius = 0;
    public static final int cost = 3;


    public SnowballTower(RectanglePoints rect, PlacementManager placementManager) {
        super(rect, placementManager);
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

    public int getCost(){
        return this.cost;
    }


    public void drawTower(Canvas canvas, AssetManager asset){
        //Bottom shadow
        paint.setARGB(17, 10, 10, 10);
        canvas.drawCircle(left + DefenceView.tileWidth, top + DefenceView.tileWidth*1.5f,  DefenceView.tileWidth, paint);

        canvas.drawBitmap(asset.SNOWMANTOWER, left, top, null);
    }



    public void drawProjectile(Canvas canvas, AssetManager asset){
        //Snowball tower doesn't attack.
    }


    //Constantly called by DefenceView towerHandler. Look for nearby enemies and attack!
    public synchronized Enemy[] watch(Enemy[] enemies, int waveID) {
        return enemies;
    }

    public synchronized Enemy updateProjectile(Enemy enemy){
        return enemy;
    }
}
