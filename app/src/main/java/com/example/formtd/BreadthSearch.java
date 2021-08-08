package com.example.formtd;

import android.graphics.Point;

import java.util.ArrayList;

public class BreadthSearch {
    private ArrayList<Point> enemyWayPoints;
    //pass all needed params in constructor
    public BreadthSearch(){
        enemyWayPoints = new ArrayList<>();
    }

    //Will calculate and return the path
    public ArrayList<Point> getUpToDatePath(){
        //Use DefenceView.grid to access grid info
        //TODO make test path, then pass it to the wave to follow it.
        enemyWayPoints = new ArrayList<>();
        enemyWayPoints.add(new Point(DefenceView.grid[3][10].x, DefenceView.grid[3][10].y));
        enemyWayPoints.add(new Point(DefenceView.grid[6][12].x, DefenceView.grid[6][12].y));
        enemyWayPoints.add(new Point(DefenceView.grid[14][4].x, DefenceView.grid[14][4].y));
        enemyWayPoints.add(new Point(DefenceView.grid[27][10].x, DefenceView.grid[27][10].y));
        return enemyWayPoints;
    }

}
