package com.example.formtd;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.example.formtd.enemies.Enemy;

import java.util.ArrayList;

//Controls the spawn waves. Note: uses static grid from DefenceView.
public class Wave {
    private Paint paint;
    private final Handler handler;
    private Runnable runnable;
    public Enemy[] enemy;
    public ArrayList<Point> enemyWayPoints;
    private int enemyAmount;
    private int enemiesGone;
    public boolean active;          //If the wave is active (if active, draw and do things, otherwise ignore wave)
    private int enemySpacing = 500;  //This is the time difference in the handler/timer
    private int x = DefenceView.centerXGrid;
    private int y = 0;
    private int currentWaypoint;

    //Takes a new Enemy() and amount of enemies in the wave.
    public Wave(Enemy enemyType, int enemyAmount){
        //This paint is for the shadow
        paint = new Paint();
        paint.setARGB(15, 10, 10, 10);
        handler = HandlerCompat.createAsync(Looper.getMainLooper());
        enemyWayPoints = DefenceView.breadthSearch.getUpToDatePath();
        //Enemy array
        this.enemyAmount = enemyAmount;
        this.enemy = new Enemy[enemyAmount];
        for (int i = 0; i < enemyAmount; i++) {
            this.enemy[i] = enemyType;
        }
        this.active = false;    //If this wave is active. If not active, it will not be drawn.
        this.currentWaypoint = 0;   //Current spot in enemyPath array.
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
                System.out.println("were false bois!");
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
            canvas.drawCircle(x + DefenceView.tileWidth/2, y + DefenceView.tileWidth/2, DefenceView.tileWidth/3, paint);
            canvas.drawBitmap(enemy.art, x - enemy.art.getWidth()/6, y - enemy.art.getHeight()/2, null);
        }




        //Eventually have a loop that draws each enemy
        //Handler that constantly updates enemy positions.
        runnable = new Runnable() {
            public void run() {

                if (y >= DefenceView.grid[DefenceView.grid.length-1][0].y) { //If over last waypoint, see if the enemy has leaked.
                    DefenceView.lives -= 1;
                    checkEndWave();
                }
                else if(currentWaypoint < enemyWayPoints.size()) {   //Make sure not to go over arraylast.
                    if (y == enemyWayPoints.get(currentWaypoint).y && x == enemyWayPoints.get(currentWaypoint).x) { //If enemy has reached waypoint:
                        currentWaypoint++;     //Increment to next waypoint
                    }
                    else if(x < enemyWayPoints.get(currentWaypoint).x){
                        x++;
                    }
                    else if(x > enemyWayPoints.get(currentWaypoint).x){
                        x--;
                    }
                    else if(y < enemyWayPoints.get(currentWaypoint).y){
                        y++;
                    }
                    else if(y > enemyWayPoints.get(currentWaypoint).y){
                        y--;
                    }
                }
                else{   //else if health <= 0...
                    //todo else if enemy's health is 0 then checkEndWave(); and reward gold.
                }
            }
        };
        handler.postDelayed(runnable, enemy[0].animDelay); //Start animation
    }

}
