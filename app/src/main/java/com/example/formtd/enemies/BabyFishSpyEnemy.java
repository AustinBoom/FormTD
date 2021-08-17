package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;
import com.example.formtd.DefenceView;

public class BabyFishSpyEnemy  extends Enemy{
    public Bitmap art;
    public final int enemySpacing = 170;
    public final int movementSpeed = 1;
    public int health = 50000;
    public int goldReward = 50;

    public BabyFishSpyEnemy(AssetManager asset) {
        super(asset);
        super.setArt(asset.BABYFISHSPYENEMY);
        super.setEnemySpacing(enemySpacing);
        super.setHealth((int) (health * DefenceView.difficultyModifier.getEnemyHealthModifier()));
        super.setGoldReward(goldReward);
        super.setMovementSpeed(movementSpeed);
    }
}
