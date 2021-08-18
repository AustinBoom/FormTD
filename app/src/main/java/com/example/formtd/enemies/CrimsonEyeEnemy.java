package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;
import com.example.formtd.DefenceView;

public class CrimsonEyeEnemy extends Enemy{
    public Bitmap art;
    public final int enemySpacing = 300;
    public final int movementSpeed = 1;
    public int health = 6000000;
    public int goldReward = 500;

    public CrimsonEyeEnemy(AssetManager asset) {
        super(asset);
        super.setArt(asset.CRIMSONEYE);
        super.setEnemySpacing(enemySpacing);
        super.setHealth((int) (health * DefenceView.difficultyModifier.getEnemyHealthModifier()));
        super.setGoldReward(goldReward);
        super.setMovementSpeed(movementSpeed);
    }
}