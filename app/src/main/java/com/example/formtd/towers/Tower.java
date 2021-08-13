package com.example.formtd.towers;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.example.formtd.DefenceView;
import com.example.formtd.PlacementManager;
import com.example.formtd.RectanglePoints;
import com.example.formtd.enemies.Enemy;


//Abstract tower class, holds basic tower functions, specific tower will be created upon child instantiation.
public abstract class Tower {
    private final Handler towerHandler;
    private Runnable towerRunnable;
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
    public int attackDamage = 1;       //Amount of damage tower does
    public int attackRange = 300;       //Radius of attack
    public int attackSpeed = 2000;       //Time between attacks
    public int projectileSpeed = 6;    //Speed of projectile animation
    public int tolerance = 20;
    private int projectileX;
    private int projectileY;


    public Tower(RectanglePoints rect, PlacementManager placementManager){
        this.towerHandler = HandlerCompat.createAsync(Looper.getMainLooper());
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
    }

    public void drawProjectile(Canvas canvas){
        //Only draw projectile when projecting. Otherwise don't draw.
        if(projecting) {
            paint.setARGB(10, 255, 0, 255);
            canvas.drawCircle(towerCenterX, towerCenterY, attackRange, paint);

            paint.setARGB(255, 255, 0, 0);
            canvas.drawCircle(projectileX, projectileY, 10, paint);
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

        //Adjust projectile position    //TODO: make the tolerance the radius of the bitmap image!
        if ((Math.abs(projectileX-enemy.x) < tolerance) && (Math.abs(projectileY-enemy.y) < tolerance)) { //If projectile has reached enemy, then clear it.
            enemy.health -= attackDamage;
            projecting = false;
            aggroEnemy = -1;
        }
        else if(projectileX < DefenceView.xGridStart || projectileX > DefenceView.xGridEnd || projectileY < DefenceView.yGridStart || projectileY > DefenceView.yGridEnd){ //If out of bounds it's a miss!
            projecting = false;
            aggroEnemy = -1;
        }
        else if(!enemy.alive){  //If enemy is already dead, then quit trying to attack it!
            projecting = false;
            aggroEnemy = -1;
        }
        else{
            projectileX += velocityX;
            projectileY += velocityY;
         }

        return enemy;
    }

}


//    //Constantly called by DefenceView towerHandler. Look for nearby enemies and attack!
//    public synchronized Enemy[] watch(Enemy[] enemies) {     //TODO::::::::: Try returning enemy[] to update new enemy health!
//        //Get pythagorean values
//        double a;
//        double b;
//        double c;
//
//        //Check each enemy in range. If found an enemy, set aggro
//        if (aggroEnemy == null) {
//            for (Enemy enemy : enemies) {
//                a = Math.abs(towerCenterX - enemy.x) * Math.abs(towerCenterX - enemy.x);
//                b = Math.abs(towerCenterY - enemy.y) * Math.abs(towerCenterY - enemy.y);
//                c = attackRange * attackRange;
//
//                if (Math.sqrt(a + b) < Math.sqrt(c)) { //If enemy is in range,
//                    System.out.println("WE GOT AGGRO!");
//                    aggroEnemy = enemy;
//                }
//            }
//        } else { //enemy already aggro'd, attack until either dead or out of range.
//            a = Math.abs(towerCenterX - aggroEnemy.x) * Math.abs(towerCenterX - aggroEnemy.x);
//            b = Math.abs(towerCenterY - aggroEnemy.y) * Math.abs(towerCenterY - aggroEnemy.y);
//            c = attackRange * attackRange;
//            if ((Math.sqrt(a + b) < Math.sqrt(c)) && aggroEnemy.health > 0 && aggroEnemy.alive) { //If enemy is in range, and alive, attack!
//                //attack!
//                this.projecting = true;
//                updateProjectile(aggroEnemy);
//
//            } else {   //Stop attacking and lose aggro
//                aggroEnemy = null;
//                this.projecting = false;
//                projectileX = towerCenterX;
//                projectileY = towerCenterY;
//            }
//        }
//
//        return enemies;
//    }