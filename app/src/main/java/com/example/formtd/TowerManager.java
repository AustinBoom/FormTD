package com.example.formtd;

import android.graphics.Point;

public class TowerManager {
    Point[][] grid;
    private boolean[][] spotAvailable;

    public TowerManager(Point[][] grid) {
        this.grid = grid;
        this.spotAvailable = new boolean[grid.length][grid[0].length];
        for (int y = 0; y < spotAvailable.length; y++) {
            for (int x = 0; x < spotAvailable[0].length; x++) {
                spotAvailable[y][x] = true;
            }
        }
    }
}
