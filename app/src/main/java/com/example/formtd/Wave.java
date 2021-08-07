package com.example.formtd;

import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.example.formtd.enemies.Enemy;

//Controls the spawn waves. Note: uses static grid from DefenceView.
public class Wave {
    private final Handler handler;
    private Runnable runnable;
    public Enemy enemy;
    private int enemyAmount;
    public boolean active;          //If the wave is active (if active, draw and do things, otherwise ignore wave)
    private int enemySpacing = 50;  //todo probably remove this and instead use a timer/handler to spawn each seperate enemy.
    private int x = DefenceView.centerXGrid;
    private int y = 0;

    //Takes a new Enemy() and amount of enemies in the wave.
    public Wave(Enemy enemy, int enemyAmount){
        handler = HandlerCompat.createAsync(Looper.getMainLooper());
        this.enemy = enemy;
        this.enemyAmount = enemyAmount;
        this.active = false;
    }

    public void startWave(){
        this.active = true;

        //DO CALCULATIONS HERE. Eventually have array (or other data structure) that calculates each enemies position.
        //Maybe do arraylist since there may be a delay in each enemy spawn, so then the drawwave can use that arraylist to draw all current enemies.


        //Implement A* algorithm here.
    }

    public void endWave(){
        this.active = false;
    }

    //Updates the enemies path; //todo make the enemy go to each coordinate in the path
    public void updatePath(){}

    public void drawWave(Canvas canvas){
        //Eventually have a loop that draws each enemy
        canvas.drawBitmap(enemy.art, x - enemy.art.getWidth()/2, y, null);

        //Handler that constantly updates enemy positions.
         runnable = new Runnable() {
            public void run() {
                y += 2;
            }
        };
        handler.postDelayed(runnable, enemy.animDelay); //Start animation
    }

}
