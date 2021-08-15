package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;

public class MinerEnemy extends Enemy{
    public final int movementSpeed = 1;
    public int health = 100;
    public int goldReward = 3;
    public Bitmap art;
    public MinerEnemy(AssetManager asset) {
        super(asset);
        super.setArt(asset.MINER);
        super.setHealth(health);
        super.setGoldReward(goldReward);
        super.setMovementSpeed(movementSpeed);
    }
}
