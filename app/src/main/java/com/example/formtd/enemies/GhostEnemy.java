package com.example.formtd.enemies;

import android.graphics.Bitmap;

import com.example.formtd.AssetManager;

public class GhostEnemy extends Enemy{
    public final int movementSpeed = 1;
    public int health = 10;
    public int goldReward = 1;
    public Bitmap art;
    public GhostEnemy(AssetManager asset, Bitmap art) {
        super(asset, art);
    }
}
