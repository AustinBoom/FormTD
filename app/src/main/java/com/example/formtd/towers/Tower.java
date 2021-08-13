package com.example.formtd.towers;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.formtd.PlacementManager;
import com.example.formtd.RectanglePoints;
import com.example.formtd.enemies.Enemy;


//Abstract tower class, holds basic tower functions, specific tower will be created upon child instantiation.
public abstract class Tower {
    PlacementManager placementManager;
    Paint paint;
    protected int left;
    protected int top;
    protected int right;
    protected int bottom;
    public int towerCenterX;
    public int towerCenterY;
    public Enemy aggroEnemy;
    public boolean projecting;
    public int attackDamage = 1;       //Amount of damage tower does
    public int attackRange = 300;       //Radius of attack
    public int attackSpeed = 100;       //Time between attacks
    public int projectileSpeed = 30;    //Speed of projectile animation
    private int projectileX;
    private int projectileY;


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
        this.projecting = false;
        this.projectileX = towerCenterX;
        this.projectileY = towerCenterY;
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
            System.out.println("projecting!");
            paint.setARGB(20, 255, 0, 255);
            canvas.drawCircle(towerCenterX, towerCenterY, attackRange, paint);

            paint.setARGB(255, 255, 0, 0);
            canvas.drawCircle(projectileX, projectileY, 10, paint);
        }
    }

    //Constantly called by DefenceView towerHandler. Look for nearby enemies and attack!
    public synchronized Enemy[] watch(Enemy[] enemies) {     //TODO::::::::: Try returning enemy[] to update new enemy health!
        //Get pythagorean values
        double a;
        double b;
        double c;

        //Check each enemy in range. If found an enemy, set aggro
        if (aggroEnemy == null) {
            for (Enemy enemy : enemies) {
                a = Math.abs(towerCenterX - enemy.x) * Math.abs(towerCenterX - enemy.x);
                b = Math.abs(towerCenterY - enemy.y) * Math.abs(towerCenterY - enemy.y);
                c = attackRange * attackRange;

                if (Math.sqrt(a + b) < Math.sqrt(c)) { //If enemy is in range,
                    System.out.println("WE GOT AGGRO!");
                    aggroEnemy = enemy;
                }
            }
        } else { //enemy already aggro'd, attack until either dead or out of range.
            a = Math.abs(towerCenterX - aggroEnemy.x) * Math.abs(towerCenterX - aggroEnemy.x);
            b = Math.abs(towerCenterY - aggroEnemy.y) * Math.abs(towerCenterY - aggroEnemy.y);
            c = attackRange * attackRange;
            if ((Math.sqrt(a + b) < Math.sqrt(c)) && aggroEnemy.health > 0 && aggroEnemy.alive) { //If enemy is in range, and alive, attack!
                //attack!
                System.out.println("WE ATTACK!");
                //this.projecting = true;
                updateProjectile(aggroEnemy);
                //todo Make handler for projectile range and attack damage update here


            } else {   //Stop attacking and lose aggro
                System.out.println("WE STOP ATTACK!");
                aggroEnemy = null;
                this.projecting = false;
                projectileX = towerCenterX;
                projectileY = towerCenterY;
            }
        }

        return enemies;
    }

    private synchronized void updateProjectile(Enemy enemy){
        //Make the projectile aim the correct way
        int xModifier = 0;
        int yModifier = 0;
        if(Math.abs(Math.abs(projectileX-enemy.x) - Math.abs(projectileY-enemy.y)) < 5){
            xModifier = 0;
            yModifier = 0;
        }
        else if(Math.abs(projectileX-enemy.x) < Math.abs(projectileY-enemy.y)){
            yModifier = 1;
        }
        else if(Math.abs(projectileX-enemy.x) > Math.abs(projectileY-enemy.y)){
            xModifier = 1;
        }

        //Adjust projectile position
        if ((Math.abs(projectileX-enemy.x) < 5) && (Math.abs(projectileY-enemy.y) < 5)) { //If projectile has reached enemy, then clear it.
            //todo Take enemies health here!
            projecting = false;
        }
        else{
            if (projectileX < enemy.x) {
                projectileX += 4 + xModifier;
            }
            else if (projectileX > enemy.x) {
                projectileX -= 4 - xModifier;
            }
            if (projectileY < enemy.y) {
                projectileY += 4 + yModifier;
            }
            else if (projectileY > enemy.y) {
                projectileY -= 4 - yModifier;
            }
         }
    }

}
