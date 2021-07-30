package com.example.formtd;

import android.graphics.Point;
import android.util.Log;

public class TowerManager {
    Point[][] grid;
    private boolean[][] spotAvailable;
    private int tileWidth;

    public TowerManager(Point[][] grid, int tileWidth) {
        this.grid = grid;
        this.tileWidth = tileWidth;
        this.spotAvailable = new boolean[grid.length][grid[0].length];
        for (int y = 0; y < spotAvailable.length; y++) {
            for (int x = 0; x < spotAvailable[0].length; x++) {
                spotAvailable[y][x] = true;
            }
        }
        //Make the spawn, mid and endpoints unavailable spots.
        for (int x = 0; x < spotAvailable[0].length; x++) {
            spotAvailable[0][x] = false;
            spotAvailable[spotAvailable.length-1][x] = false;
        }
        spotAvailable[spotAvailable.length/2][spotAvailable[0].length/2] = false;
        spotAvailable[spotAvailable.length/2][spotAvailable[0].length/2 + 1] = false;
        spotAvailable[spotAvailable.length/2 + 1][spotAvailable[0].length/2] = false;
        spotAvailable[spotAvailable.length/2 + 1][spotAvailable[0].length/2 + 1] = false;


//        //test output
//        for (int y = 0; y < spotAvailable.length; y++) {
//            for (int x = 0; x < spotAvailable[0].length; x++) {
//                if (spotAvailable[y][x])
//                    System.out.print("T ");
//                else
//                    System.out.print("F ");
//            }
//            System.out.println();
//        }
    }

    public boolean[][] getAllSpotsAvailable(){
        return spotAvailable;
    }
    public boolean checkSpotAvailability(int left, int top){
        //Get index by going to coordinate
        boolean lefttop = false;
        boolean leftbottom = false;
        boolean righttop = false;
        boolean rightbottom = false;
        int right = left + tileWidth;
        int bottom = top + tileWidth;

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if(grid[y][x].x == left && grid[y][x].y == top)
                    lefttop = spotAvailable[y][x];
                if(grid[y][x].x == left && grid[y][x].y == bottom)
                    leftbottom = spotAvailable[y][x];
                if(grid[y][x].x == right && grid[y][x].y == top)
                    righttop = spotAvailable[y][x];
                if(grid[y][x].x == right && grid[y][x].y == bottom)
                    rightbottom = spotAvailable[y][x];
            }
        }

        return lefttop & leftbottom & righttop & rightbottom;
    }
}
