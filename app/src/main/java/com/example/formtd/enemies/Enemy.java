package com.example.formtd.enemies;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.example.formtd.AssetManager;
import com.example.formtd.BreadthSearch;
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
    public int x = DefenceView.centerXGrid;
    public int y = DefenceView.yGridStart;
    public ArrayList<Point> enemyWayPoints;
    public static BreadthSearch breadthSearch;      //Figures out the path to take!

    public Enemy(AssetManager asset){
        this.asset = asset;
        art = asset.GHOST;
        breadthSearch = new BreadthSearch();
    }

}
