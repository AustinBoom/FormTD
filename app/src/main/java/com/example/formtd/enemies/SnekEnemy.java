package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;
import com.example.formtd.DefenceView;

public class SnekEnemy  extends Enemy{
    public Bitmap art;
    public final int enemySpacing = 130;
    public final int movementSpeed = 1;
    public int health = 6500;
    public int goldReward = 25;

    public SnekEnemy(AssetManager asset) {
        super(asset);
        super.setArt(asset.SNEK);
        super.setEnemySpacing(enemySpacing);
        super.setHealth((int) (health * DefenceView.difficultyModifier.getEnemyHealthModifier()));
        super.setGoldReward(goldReward);
        super.setMovementSpeed(movementSpeed);
    }
}
