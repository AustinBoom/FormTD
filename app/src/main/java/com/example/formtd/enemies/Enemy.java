package com.example.formtd.enemies;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.example.formtd.AssetManager;
import com.example.formtd.DefenceView;

import java.util.ArrayList;

//Abstract enemy class, holds basic enemy functions, specific enemy will be created upon child instantiation.
public abstract class Enemy {
    AssetManager asset;
    public final int movementSpeed = 1;
    public int animDelay = 20;   //For extra slow movement or "jumping"
    public int health = 10;
    public int goldReward = 1;
    public boolean alive = true;
    public Bitmap art;
    public int currentWayPoint = 0;
    //todo go back to this spawn. Currently changed for testing purposes.
    //public int x = DefenceView.centerXGrid;
    //public int y = 0;

    public int x = DefenceView.grid[5][5].x + 10;
    public int y = DefenceView.grid[5][5].y - 20;

    public ArrayList<Point> enemyWayPoints;
    //TODO put a bitmap of a default enemy here
    public Enemy(AssetManager asset){
        this.asset = asset;
        art = asset.GHOST;
    }

}
