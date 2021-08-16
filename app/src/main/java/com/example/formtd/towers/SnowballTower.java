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
    private double angle;
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
    public int attackDamage = 1;       //Amount of damage tower does
    public int attackRange = 250;       //Radius of attack
    public int projectileSpeed = 3;    //Speed of projectile animation
    public int tolerance = 4;           //Multiplies by projectile Radius
    public int projectileRadius = 6;
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

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public int getProjectileSpeed() {
        return projectileSpeed;
    }

    public int getTolerance() {
        return tolerance;
    }

    public int getProjectileRadius() {
        return projectileRadius;
    }

    public void drawTower(Canvas canvas, AssetManager asset){
        //Bottom shadow
//        paint.setARGB(25, 5, 5, 200);
//        canvas.drawRoundRect(left, top, right, bottom, 25, 25, paint);

        paint.setARGB(17, 10, 10, 10);
        canvas.drawCircle(left + DefenceView.tileWidth, top + DefenceView.tileWidth*1.5f,  DefenceView.tileWidth, paint);


        canvas.drawBitmap(asset.SNOWMAN, left, top, null);
    }



    public void drawProjectile(Canvas canvas, AssetManager asset){
        //Only draw projectile when projecting. Otherwise don't draw.
        if(projecting) {
            // paint.setARGB(10, 255, 0, 255);   //uncomment to see attack range.
            // canvas.drawCircle(towerCenterX, towerCenterY, attackRange, paint);


             paint.setARGB(11, 20, 20, 45);   //Shadow
             canvas.drawCircle(projectileX + DefenceView.tileWidth/4 +4, projectileY - DefenceView.tileWidth/6 +7, projectileRadius, paint);
             paint.setARGB(255, 220, 220, 255);
             canvas.drawCircle(projectileX + DefenceView.tileWidth/4, projectileY - DefenceView.tileWidth/6, projectileRadius, paint);  //Adds to 10 account for enemy's animation offset.
        }
    }



    //Constantly called by DefenceView towerHandler. Look for nearby enemies and attack!
    public synchronized Enemy[] watch(Enemy[] enemies, int waveID) {
        //Get pythagorean values
        double a;
        double b;
        double c;

        //Check each enemy in range. If found an enemy, set aggro
        for (int i = 0; i < enemies.length; i++){
            a = Math.abs(towerCenterX - enemies[i].x) * Math.abs(towerCenterX - enemies[i].x);
            b = Math.abs(towerCenterY - enemies[i].y) * Math.abs(towerCenterY - enemies[i].y);
            c = attackRange * attackRange;

            if (Math.sqrt(a + b) < Math.sqrt(c) && enemies[i].health > 0 && enemies[i].alive && aggroEnemy == -1) { //If enemy is in range,
                //attack!
                aggroEnemy = i;
                currentWaveID = waveID;
                projectileX = towerCenterX;
                projectileY = towerCenterY;
            }
        }

        //If an enemy is aggro'd, attack!
        if(aggroEnemy != -1 && waveID == currentWaveID){
            this.projecting = true;
            enemies[aggroEnemy] = updateProjectile(enemies[aggroEnemy]);
        }


        return enemies;
    }

    private synchronized Enemy updateProjectile(Enemy enemy){
        int velocityX = enemy.x - towerCenterX;
        int velocityY = enemy.y - towerCenterY;
        double length = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        velocityX *= projectileSpeed/length;
        velocityY *= projectileSpeed/length;
        angle = Math.atan2(enemy.y - towerCenterY, enemy.x - towerCenterX);  //For bitmap rotation!

        //Adjust projectile position
        if ((Math.abs(projectileX - enemy.x) < tolerance * projectileRadius) && (Math.abs(projectileY - enemy.y) < tolerance * projectileRadius)) { //If projectile has reached enemy, then clear it.
            enemy.health -= attackDamage;
            projecting = false;
            aggroEnemy = -1;
        } else if (projectileX < DefenceView.xGridStart || projectileX > DefenceView.xGridEnd || projectileY < DefenceView.yGridStart || projectileY > DefenceView.yGridEnd) { //If out of bounds it's a miss!
            projecting = false;
            aggroEnemy = -1;
        } else if (!enemy.alive) {  //If enemy is already dead, then quit trying to attack it!
            projecting = false;
            aggroEnemy = -1;
        } else {
            projectileX += velocityX;
            projectileY += velocityY;
        }



        return enemy;
    }
}
