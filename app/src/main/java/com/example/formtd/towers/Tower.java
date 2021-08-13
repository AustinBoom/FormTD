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
    public int towerCenterX;
    public int towerCenterY;
    public Enemy aggroEnemy;
    public int attackDamage = 1;       //Amount of damage tower does
    public int attackRange = 300;       //Radius of attack
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
        this.towerCenterX = (left + right)/2;
        this.towerCenterY = (top + bottom)/2;
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

    //Constantly called by DefenceView towerHandler. Look for nearby enemies and attack!
    public void watch(Enemy[] enemies){
        //Get pythagorean values
        double a;
        double b;
        double c;

        //Check each enemy in range. If found an enemy, set aggro
        if(aggroEnemy == null) {
            for (Enemy enemy : enemies) {
                a = Math.abs(towerCenterX - enemy.x) * Math.abs(towerCenterX - enemy.x);
                b = Math.abs(towerCenterY - enemy.y) * Math.abs(towerCenterY - enemy.y);
                c = attackRange * attackRange;

                if (Math.sqrt(a+b) < Math.sqrt(c)) { //If enemy is in range,
                    System.out.println("WE GOT AGGRO!");
                    aggroEnemy = enemy;
                }
            }
        }
        else{ //enemy already aggro'd, attack until either dead or out of range.
            a = Math.abs(towerCenterX - aggroEnemy.x) * Math.abs(towerCenterX - aggroEnemy.x);
            b = Math.abs(towerCenterY - aggroEnemy.y) * Math.abs(towerCenterY - aggroEnemy.y);
            c = attackRange * attackRange;
            if ((Math.sqrt(a+b) < Math.sqrt(c)) && aggroEnemy.health > 0) { //If enemy is in range, and alive, attack!
                //attack!
                System.out.println("WE ATTACK!");
            }
            else{
                System.out.println("WE STOP ATTACK!");
                aggroEnemy = null;
            }
        }
    }

}
