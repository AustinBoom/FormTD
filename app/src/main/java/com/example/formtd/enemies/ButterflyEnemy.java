package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;
import com.example.formtd.DefenceView;

public class ButterflyEnemy extends Enemy{
    public Bitmap art;
    public final int enemySpacing = 170;
    public final int movementSpeed = 1;
    public int health = 100000;
    public int goldReward = 250;

    public ButterflyEnemy(AssetManager asset) {
        super(asset);
        super.setArt(asset.BUTTERFLY);
        super.setEnemySpacing(enemySpacing);
        super.setHealth((int) (health * DefenceView.difficultyModifier.getEnemyHealthModifier()));
        super.setGoldReward(goldReward);
        super.setMovementSpeed(movementSpeed);
    }
}