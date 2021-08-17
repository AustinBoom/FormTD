package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;
import com.example.formtd.DefenceView;

public class WaterGhostEnemy extends Enemy{
    public Bitmap art;
    public final int enemySpacing = 140;
    public final int movementSpeed = 1;
    public int health = 200;
    public int goldReward = 5;

    public WaterGhostEnemy(AssetManager asset) {
        super(asset);
        super.setArt(asset.WATERGHOST);
        super.setEnemySpacing(enemySpacing);
        super.setHealth((int) (health * DefenceView.difficultyModifier.getEnemyHealthModifier()));
        super.setGoldReward(goldReward);
        super.setMovementSpeed(movementSpeed);
    }
}
