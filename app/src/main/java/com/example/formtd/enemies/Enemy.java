package com.example.formtd.enemies;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.example.formtd.AssetManager;
import com.example.formtd.BreadthSearch;
import com.example.formtd.DefenceView;

import java.util.ArrayList;
import java.util.UUID;

//Abstract enemy class, holds basic enemy functions, specific enemy will be created upon child instantiation.
public abstract class Enemy {
    AssetManager asset;
    public boolean alive = true;
    public boolean reachedCenter = false;
    public int currentWayPoint = 0;
    public int x = DefenceView.centerXGrid;
    public int y = DefenceView.yGridStart;
    public ArrayList<Point> enemyWayPoints;
    public static BreadthSearch breadthSearch;      //Figures out the path to take!



    //These get updated by child.
    public Bitmap art;
    public int health = 100;
    public int goldReward = 1;
    public int movementSpeed = 1;

    public Enemy(AssetManager asset){
        this.asset = asset;
        this.art = art;
        breadthSearch = new BreadthSearch();
    }


    public void kill(){
        if(alive) { //Only reward gold once.
            DefenceView.gold += goldReward;
        }
        alive = false;
    }


    public void setArt(Bitmap art) {
        this.art = art;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setGoldReward(int goldReward) {
        this.goldReward = goldReward;
    }

    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

}
