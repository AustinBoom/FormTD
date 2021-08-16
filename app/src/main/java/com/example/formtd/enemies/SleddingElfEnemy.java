package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;

public class SleddingElfEnemy extends Enemy{
    public Bitmap art;
    public final int enemySpacing = 110;
    public final int movementSpeed = 1;
    public int health = 100;
    public int goldReward = 10;

    public SleddingElfEnemy(AssetManager asset) {
        super(asset);
        super.setArt(asset.SLEDDINGELF);
        super.setEnemySpacing(enemySpacing);
        super.setHealth(health);
        super.setGoldReward(goldReward);
        super.setMovementSpeed(movementSpeed);
    }
}
