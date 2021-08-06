package com.example.formtd;

import android.graphics.Canvas;

import com.example.formtd.enemies.Enemy;

//Controls the spawn waves. Note: uses static grid from DefenceView.
public class Wave {
    public Enemy enemy;
    private int enemyAmount;

    //Takes a new Enemy() and amount of enemies in the wave.
    public Wave(Enemy enemy, int enemyAmount){
        this.enemy = enemy;
        this.enemyAmount = enemyAmount;
    }

    public void startWave(){
        System.out.println(DefenceView.grid[0][0].x);
        //Draw things using enemy data, probably have dijkstra's here too.
    }
}
