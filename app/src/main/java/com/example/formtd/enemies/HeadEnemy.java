package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;
import com.example.formtd.DefenceView;

public class HeadEnemy  extends Enemy{
    public Bitmap art;
    public final int enemySpacing = 120;
    public final int movementSpeed = 1;
    public int health = 250;
    public int goldReward = 5;

    public HeadEnemy(AssetManager asset) {
        super(asset);
        super.setArt(asset.HEAD);
        super.setEnemySpacing(enemySpacing);
        super.setHealth((int) (health * DefenceView.difficultyModifier.getEnemyHealthModifier()));
        super.setGoldReward(goldReward);
        super.setMovementSpeed(movementSpeed);
    }
}

