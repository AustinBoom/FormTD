package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;
import com.example.formtd.DefenceView;

public class SnekEnemy  extends Enemy{
    public Bitmap art;
    public final int enemySpacing = 120;
    public final int movementSpeed = 1;
    public int health = 5000;
    public int goldReward = 30;

    public SnekEnemy(AssetManager asset) {
        super(asset);
        super.setArt(asset.SNEK);
        super.setEnemySpacing(enemySpacing);
        super.setHealth((int) (health * DefenceView.difficultyModifier.getEnemyHealthModifier()));
        super.setGoldReward(goldReward);
        super.setMovementSpeed(movementSpeed);
    }
}
