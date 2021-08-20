package com.example.formtd.towers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.formtd.AssetManager;
import com.example.formtd.DefenceView;
import com.example.formtd.PlacementManager;
import com.example.formtd.RectanglePoints;
import com.example.formtd.enemies.Enemy;
import java.util.Random;


public class FruitStandTower extends Tower{
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
    Matrix matrix = new Matrix();       //For projectile angle

    //To cycle through different fruits.
    Bitmap[] currentFruit;
    private int current;
    Random ran;

    //Customizables
    public static int attackDamage = 100;       //Amount of damage tower does
    public static int attackRange = 700;       //Radius of attack
    public static int projectileSpeed = 12;    //Speed of projectile animation
    public static int tolerance = 3;
    public static int projectileRadius = 6;
    public static final int cost = 200;

    public FruitStandTower(RectanglePoints rect, PlacementManager placementManager, AssetManager asset) {
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

        //To cycle through fruits
        currentFruit = new Bitmap[3];
        currentFruit[0] = asset.BANANA;
        currentFruit[1] = asset.WATERMELON;
        currentFruit[2] = asset.APPLE;
        current = 0;
        ran = new Random();

        //Upon creation charge gold
        DefenceView.gold -= cost;
    }


    public void drawTower(Canvas canvas, AssetManager asset){
        canvas.drawBitmap(asset.FRUITSTANDTOWER, left, top, null);
    }

    public int getCost(){
        return this.cost;
    }


    public void drawProjectile(Canvas canvas, AssetManager asset){
        //Only draw projectile when projecting. Otherwise don't draw.
        if(projecting) {
            // paint.setARGB(10, 255, 0, 255);   //uncomment to see attack range.
            // canvas.drawCircle(towerCenterX, towerCenterY, attackRange, paint);

//            //Shadow
//            paint.setARGB(11, 20, 20, 45);   //Shadow
//            canvas.drawCircle(projectileX + DefenceView.tileWidth/4 +4, projectileY - DefenceView.tileWidth/6 +7, projectileRadius, paint);

            //Arrow
            matrix.setRotate(angle, asset.ARROWPROJECTILE.getWidth()/2, asset.ARROWPROJECTILE.getHeight()/2);
            matrix.postTranslate(projectileX + DefenceView.tileWidth/4, projectileY - DefenceView.tileWidth/5);
            canvas.drawBitmap(currentFruit[current], matrix, null);

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

            if (Math.sqrt(a + b) < Math.sqrt(c) && enemies[i].health > 0 && enemies[i].alive && aggroEnemy == -1 && enemies[i].y > 0) { //If enemy is in range,
                //attack!
                aggroEnemy = i;
                currentWaveID = waveID;
                projectileX = towerCenterX;
                projectileY = towerCenterY;
                current = ran.nextInt(currentFruit.length);
            }
        }

        //If an enemy is aggro'd, attack!
        if(aggroEnemy != -1 && waveID == currentWaveID){
            this.projecting = true;
            enemies[aggroEnemy] = updateProjectile(enemies[aggroEnemy]);
        }


        return enemies;
    }

    public synchronized Enemy updateProjectile(Enemy enemy){
        int velocityX = enemy.x - towerCenterX;
        int velocityY = enemy.y - towerCenterY;
        double length = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        velocityX *= projectileSpeed/length;
        velocityY *= projectileSpeed/length;
        angle = (float) Math.atan2(enemy.y - towerCenterY, enemy.x - towerCenterX) *60;  //For bitmap rotation!
        angle += projectileY + projectileX; //Make them spin!

        //Adjust projectile position
        if ((Math.abs(projectileX-enemy.x) < tolerance * projectileRadius) && (Math.abs(projectileY-enemy.y) < tolerance * projectileRadius)) { //If projectile has reached enemy, then clear it.
            enemy.health -= attackDamage;
            projecting = false;
            aggroEnemy = -1;
            //Cycle through different fruits.
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
