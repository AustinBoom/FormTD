package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;
import com.example.formtd.DefenceView;

public class GhostEnemy extends Enemy{
    public int movementSpeed = 1;
    public int health = 10;
    public int goldReward = 1;
    public Bitmap art;
    public GhostEnemy(AssetManager asset) {
        super(asset);
        this.art = asset.GHOST;
    }
}
