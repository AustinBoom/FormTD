package com.example.formtd;

import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.example.formtd.enemies.Enemy;

import java.util.ArrayList;

//Controls the spawn waves. Note: uses static grid from DefenceView.
public class Wave {
    private final Handler handler;
    private Runnable runnable;
    public Enemy enemy;
    public ArrayList<Point> enemyPath;
    private int enemyAmount;
    private int enemiesGone;
    public boolean active;          //If the wave is active (if active, draw and do things, otherwise ignore wave)
    private int enemySpacing = 500;  //This is the time difference in the handler/timer
    private int x = DefenceView.centerXGrid;
    private int y = 0;
    private int currentWaypoint;

    //Takes a new Enemy() and amount of enemies in the wave.
    public Wave(Enemy enemy, int enemyAmount){
        handler = HandlerCompat.createAsync(Looper.getMainLooper());
        enemyPath = DefenceView.breadthSearch.getUpToDatePath();
        this.enemy = enemy;
        this.enemyAmount = enemyAmount;
        this.active = false;
        this.currentWaypoint = 0;
    }

    public void startWave(){
        this.active = true;
    }

    public void checkEndWave(){
        //TODO if every enemy is past the end, then make this wave inactive, otherwise just return true.
        enemiesGone++;
        if(enemiesGone >= enemyAmount) {
            this.active = false;
        }
    }


    public void drawWave(Canvas canvas){
        //If a tower is placed, update to the new path.
        if(DefenceView.pathNeedsUpdating){
            enemyPath = DefenceView.breadthSearch.getUpToDatePath();
            DefenceView.pathNeedsUpdating = false;
        }
        canvas.drawBitmap(enemy.art, x - enemy.art.getWidth()/4, y, null);


        //Eventually have a loop that draws each enemy
        //Handler that constantly updates enemy positions.
        runnable = new Runnable() {
            public void run() {
                //If enemy has reached waypoint:
                if(currentWaypoint < enemyPath.size()) {   //Make sure not to go over arraylast.
                    if (y == enemyPath.get(currentWaypoint).y && x == enemyPath.get(currentWaypoint).x) {
                        currentWaypoint++;     //Increment to next waypoint
                    }
                    else if(y < enemyPath.get(currentWaypoint).y){
                        y++;
                    }
                    else if(y > enemyPath.get(currentWaypoint).y){
                        y--;
                    }
                    else if(x < enemyPath.get(currentWaypoint).x){
                        x++;
                    }
                    else if(x > enemyPath.get(currentWaypoint).x){
                        x--;
                    }
                }
                else{ //If over last waypoint, see if the enemy has leaked.
                    if (y >= DefenceView.grid[DefenceView.grid.length-1][0].y) {
                        DefenceView.lives -= 1;
                        checkEndWave();
                        System.out.println("Y: is " + y + " Current end y is " + DefenceView.grid[0][DefenceView.grid[0].length-1].y);
                    }
                    //todo else if enemy's health is 0 then checkEndWave(); and reward gold.
                }
            }
        };
        handler.postDelayed(runnable, enemy.animDelay); //Start animation
    }

}
