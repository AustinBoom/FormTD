package com.example.formtd;

import android.graphics.Canvas;

import com.example.formtd.enemies.Enemy;

//Controls the spawn waves. Note: uses static grid from DefenceView.
public class Wave {
    public Enemy enemy;
    private int enemyAmount;
    public boolean active;          //If the wave is active (if active, draw and do things, otherwise ignore wave)

    //Takes a new Enemy() and amount of enemies in the wave.
    public Wave(Enemy enemy, int enemyAmount){
        this.enemy = enemy;
        this.enemyAmount = enemyAmount;
        this.active = false;
    }

    public void startWave(){
        //DO CALCULATIONS HERE. Eventually have array (or other data structure) that calculates each enemies position.
        //Maybe do arraylist since there may be a delay in each enemy spawn, so then the drawwave can use that arraylist to draw all current enemies.


        //Implement A* algorithm here.
    }

    public void drawWave(Canvas canvas){
        //Eventually have a loop that draws each enemy
        canvas.drawBitmap(enemy.art, 500, 100, null);
    }
}
