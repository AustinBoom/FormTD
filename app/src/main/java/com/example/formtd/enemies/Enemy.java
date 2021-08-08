package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;

//Abstract enemy class, holds basic enemy functions, specific enemy will be created upon child instantiation.
public abstract class Enemy {
    AssetManager asset;
    public int movementSpeed = 1;
    public int animDelay = 20;   //For extra slow movement or "jumping"
    public int health = 10;
    public int goldReward = 1;
    public Bitmap art;
    //TODO put a bitmap of a default enemy here
    public Enemy(AssetManager asset){
        this.asset = asset;
        art = asset.GHOST;
    }

}
