package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;

public class GhostEnemy extends Enemy{
    public int movementSpeed = 1;
    public int health = 10;
    public int goldReward = 1;
    Bitmap ghostArt;
    public GhostEnemy(AssetManager asset) {
        super(asset);
        this.ghostArt = asset.GHOST;
    }
}
