package com.example.formtd;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.example.formtd.enemies.Enemy;
import com.example.formtd.enemies.GhostEnemy;

import java.util.ArrayList;

//Controls the spawn waves. Note: uses static grid from DefenceView.
public class Wave {
    private Paint paint;
    private AssetManager asset;
    private final Handler waveHandler;
    private Runnable waveRunnable;
    public Enemy[] enemy;
    public ArrayList<Point> enemyWayPoints;
    private int enemyAmount;
    public boolean active;          //If the wave is active (if active, draw and do things, otherwise ignore wave)
    private int enemySpacing = 100;  //This is the time difference in the handler/timer
    //private int currentWaypoint;

    //Takes a new Enemy() and amount of enemies in the wave.
    public Wave(AssetManager asset, String enemyType, int enemyAmount){
        //This paint is for the shadow
        paint = new Paint();
        paint.setARGB(15, 10, 10, 10);
        waveHandler = HandlerCompat.createAsync(Looper.getMainLooper());
        enemyWayPoints = DefenceView.breadthSearch.getUpToDatePath();
        this.asset = asset;
        //Enemy array
        this.enemyAmount = enemyAmount;
        this.enemy = new Enemy[enemyAmount];
        for (int i = 0; i < this.enemyAmount; i++) {
            this.enemy[i] = createEnemy(enemyType);
            this.enemy[i].y -= (i*enemySpacing);
        }
        this.active = false;    //If this wave is active. If not active, it will not be drawn.
//        this.currentWaypoint = 0;   //Current spot in enemyPath array.

    }

    public void startWave(){
        this.active = true;
    }

    public void checkEndWave(){
        //TODO if every enemy is past the end, then make this wave inactive, otherwise just return true.
        for (Enemy enemy : enemy) {
            if(enemy.alive){
                this.active = true;
            }
            else{
                this.active = false;
            }
        }
    }


    public void drawWave(Canvas canvas){
        //If a tower is placed, update to the new path.
        if(DefenceView.pathNeedsUpdating){
            enemyWayPoints = DefenceView.breadthSearch.getUpToDatePath();
            DefenceView.pathNeedsUpdating = false;
        }
        for (Enemy enemy: enemy) {
            canvas.drawCircle(enemy.x + DefenceView.tileWidth/2, enemy.y + DefenceView.tileWidth/2, DefenceView.tileWidth/3, paint);
            canvas.drawBitmap(enemy.art, enemy.x - enemy.art.getWidth()/6, enemy.y - enemy.art.getHeight()/2, null);
        }



        //Eventually have a loop that draws each enemy
        //Handler that constantly updates enemy positions.
        waveRunnable = new Runnable() {
            public void run() {
                for (Enemy enemy: enemy) {
                    if (enemy.y >= DefenceView.grid[DefenceView.grid.length - 1][0].y) { //If over last waypoint, see if the enemy has leaked.
                        DefenceView.lives -= 1;
                        enemy.alive = false;
                        checkEndWave();
                    } else if (enemy.currentWayPoint< enemyWayPoints.size()) {   //Make sure not to go over arraylast.
                        if (enemy.y == enemyWayPoints.get(enemy.currentWayPoint).y && enemy.x == enemyWayPoints.get(enemy.currentWayPoint).x) { //If enemy has reached waypoint:
                            enemy.currentWayPoint++;     //Increment to next waypoint
                        } else if (enemy.x < enemyWayPoints.get(enemy.currentWayPoint).x) {
                            enemy.x++;
                        } else if (enemy.x > enemyWayPoints.get(enemy.currentWayPoint).x) {
                            enemy.x--;
                        } else if (enemy.y < enemyWayPoints.get(enemy.currentWayPoint).y) {
                            enemy.y++;
                        } else if (enemy.y > enemyWayPoints.get(enemy.currentWayPoint).y) {
                            enemy.y--;
                        }
                    } else {   //else if health <= 0...
                        //todo else if enemy's health is 0 then checkEndWave(); and reward gold.
                    }
                }
            }
        };
        waveHandler.postDelayed(waveRunnable, enemy[0].animDelay); //Start animation
    }


    public Enemy createEnemy(String enemy){
        switch (enemy){
            case "ghost":
                return new GhostEnemy(asset);
            default:
                throw new Resources.NotFoundException();
        }
    }

}
