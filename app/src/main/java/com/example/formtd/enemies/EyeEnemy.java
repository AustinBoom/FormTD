package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;
import com.example.formtd.DefenceView;

public class EyeEnemy extends Enemy{
    public Bitmap art;
    public final int enemySpacing = 250;
    public final int movementSpeed = 1;
    public int health = 2000;
    public int goldReward = 30;

    public EyeEnemy(AssetManager asset) {
        super(asset);
        super.setArt(asset.EYE);
        super.setEnemySpacing(enemySpacing);
        super.setHealth((int) (health * DefenceView.difficultyModifier.getEnemyHealthModifier()));
        super.setGoldReward(goldReward);
        super.setMovementSpeed(movementSpeed);
    }
}
