package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;
import com.example.formtd.DefenceView;

public class LavaGhostEnemy  extends Enemy{
    public Bitmap art;
    public final int enemySpacing = 200;
    public final int movementSpeed = 1;
    public int health = 2500;
    public int goldReward = 20;

    public LavaGhostEnemy(AssetManager asset) {
        super(asset);
        super.setArt(asset.LAVAGHOST);
        super.setEnemySpacing(enemySpacing);
        super.setHealth((int) (health * DefenceView.difficultyModifier.getEnemyHealthModifier()));
        super.setGoldReward(goldReward);
        super.setMovementSpeed(movementSpeed);
    }
}
