package com.example.formtd;

import android.graphics.Point;

import java.util.ArrayList;

public class BreadthSearch {
    private ArrayList<Point> enemyWayPoints;
    private Point endPoint;
    private Point[] centerPoints;
    //pass all needed params in constructor
    public BreadthSearch()
    {
        //Note: start point is determined by enemy.
        endPoint = new Point(DefenceView.grid[27][10].x, DefenceView.grid[27][10].y);
        centerPoints = new Point[]{
                new Point(DefenceView.grid[DefenceView.grid.length/2][DefenceView.grid[0].length/2 - 1].x, DefenceView.grid[DefenceView.grid.length/2 - 1][DefenceView.grid[0].length/2].y),
                new Point(DefenceView.grid[DefenceView.grid.length/2][DefenceView.grid[0].length/2 - 1].x + DefenceView.tileWidth, DefenceView.grid[DefenceView.grid.length/2 - 1][DefenceView.grid[0].length/2].y),
                new Point(DefenceView.grid[DefenceView.grid.length/2][DefenceView.grid[0].length/2 - 1].x, DefenceView.grid[DefenceView.grid.length/2 - 1][DefenceView.grid[0].length/2].y + DefenceView.tileWidth),
                new Point(DefenceView.grid[DefenceView.grid.length/2][DefenceView.grid[0].length/2 - 1].x + DefenceView.tileWidth, DefenceView.grid[DefenceView.grid.length/2 - 1][DefenceView.grid[0].length/2].y + DefenceView.tileWidth)
        };
        enemyWayPoints = new ArrayList<>();
    }

    //Will calculate and return the path
    public ArrayList<Point> getUpToDatePath(){
        //TODO make breadthfirst search!
        enemyWayPoints = new ArrayList<>();

        //Some test points.
        enemyWayPoints.add(new Point(DefenceView.grid[3][10].x, DefenceView.grid[3][10].y));
        enemyWayPoints.add(new Point(DefenceView.grid[6][12].x, DefenceView.grid[6][12].y));
        enemyWayPoints.add(new Point(DefenceView.grid[14][4].x, DefenceView.grid[14][4].y));


        //Add end point
        enemyWayPoints.add(endPoint);
        return enemyWayPoints;
    }

}
