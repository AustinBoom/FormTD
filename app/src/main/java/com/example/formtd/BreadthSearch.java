package com.example.formtd;

import android.graphics.Point;

import java.util.ArrayList;

public class BreadthSearch {
    private ArrayList<Point> enemyPath;
    //pass all needed params in constructor
    public BreadthSearch(){
        enemyPath = new ArrayList<>();
    }

    //Will calculate and return the path
    public ArrayList<Point> getUpToDatePath(){
        //Use DefenceView.grid to access grid info
        //TODO make test path, then pass it to the wave to follow it.
        enemyPath = new ArrayList<>();
        enemyPath.add(new Point(DefenceView.grid[3][2].x, DefenceView.grid[3][2].y));
        enemyPath.add(new Point(DefenceView.grid[6][10].x, DefenceView.grid[6][10].y));
        enemyPath.add(new Point(DefenceView.grid[8][4].x, DefenceView.grid[8][4].y));
        return enemyPath;
    }

}
