package com.example.formtd;

import android.graphics.Point;

import java.util.ArrayList;

public class BreadthSearch {
    Grid[][] grid;
    ArrayList<Point> enemyPath;
    //pass all needed params in constructor
    public BreadthSearch(Grid[][] grid){
        this.grid = grid;
        enemyPath = new ArrayList<>();
    }

    //Will calculate and return the path
    public ArrayList<Point> getUpToDatePath(){
        //TODO make test path, then pass it to the wave to follow it.
        enemyPath.add(new Point(grid[3][2].x, grid[3][2].y));
        enemyPath.add(new Point(grid[5][6].x, grid[5][6].y));
        return null;
    }

}
