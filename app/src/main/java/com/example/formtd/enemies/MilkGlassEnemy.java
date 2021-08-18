package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;
import com.example.formtd.DefenceView;

public class MilkGlassEnemy extends Enemy{
    public Bitmap art;
    public final int enemySpacing = 190;
    public final int movementSpeed = 1;
    public int health = 50000;
    public int goldReward = 125;

    public MilkGlassEnemy(AssetManager asset) {
        super(asset);
        super.setArt(asset.GLASSOFMILK);
        super.setEnemySpacing(enemySpacing);
        super.setHealth((int) (health * DefenceView.difficultyModifier.getEnemyHealthModifier()));
        super.setGoldReward(goldReward);
        super.setMovementSpeed(movementSpeed);
    }
}