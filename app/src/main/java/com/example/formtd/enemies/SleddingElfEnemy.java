package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;
import com.example.formtd.DefenceView;

public class SleddingElfEnemy extends Enemy{
    public Bitmap art;
    public final int enemySpacing = 130;
    public final int movementSpeed = 1;
    public int health = 100;
    public int goldReward = 5;

    public SleddingElfEnemy(AssetManager asset) {
        super(asset);
        super.setArt(asset.SLEDDINGELF);
        super.setEnemySpacing(enemySpacing);
        super.setHealth((int) (health * DefenceView.difficultyModifier.getEnemyHealthModifier()));
        super.setGoldReward(goldReward);
        super.setMovementSpeed(movementSpeed);
    }
}
