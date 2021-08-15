package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;

public class GhostEnemy extends Enemy{
    public Bitmap art;
    public final int enemySpacing = 130;
    public final int movementSpeed = 1;
    public int health = 20;
    public int goldReward = 1;

    public GhostEnemy(AssetManager asset) {
        super(asset);
        super.setArt(asset.GHOST);
        super.setEnemySpacing(enemySpacing);
        super.setHealth(health);
        super.setGoldReward(goldReward);
        super.setMovementSpeed(movementSpeed);
    }
}
