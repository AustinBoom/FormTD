package com.example.formtd;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class BreadthSearch {
    private ArrayList<Point> enemyWayPoints;
    private Point endPoint;
    private Point[] centerPoints;
    private Queue<Point> frontier;
    private HashMap<Point, Point> cameFrom;
    private Point current;

    //pass all needed params in constructor
    public BreadthSearch()
    {
        //Note: start point is determined by enemy.
        centerPoints = new Point[]{
                new Point(DefenceView.grid[DefenceView.grid.length/2][DefenceView.grid[0].length/2 - 1].x, DefenceView.grid[DefenceView.grid.length/2 - 1][DefenceView.grid[0].length/2].y),
                new Point(DefenceView.grid[DefenceView.grid.length/2][DefenceView.grid[0].length/2 - 1].x + DefenceView.tileWidth, DefenceView.grid[DefenceView.grid.length/2 - 1][DefenceView.grid[0].length/2].y),
                new Point(DefenceView.grid[DefenceView.grid.length/2][DefenceView.grid[0].length/2 - 1].x, DefenceView.grid[DefenceView.grid.length/2 - 1][DefenceView.grid[0].length/2].y + DefenceView.tileWidth),
                new Point(DefenceView.grid[DefenceView.grid.length/2][DefenceView.grid[0].length/2 - 1].x + DefenceView.tileWidth, DefenceView.grid[DefenceView.grid.length/2 - 1][DefenceView.grid[0].length/2].y + DefenceView.tileWidth)
        };
        endPoint = new Point(DefenceView.grid[27][10].x, DefenceView.grid[27][10].y);

        enemyWayPoints = new ArrayList<>();
    }


    //Will calculate and return the path
    public ArrayList<Point> getUpToDatePath(Point startPos){  //todo get enemies current position
        //Initialize start conditions
        enemyWayPoints = new ArrayList<>();
        frontier = new LinkedList<>();
        cameFrom = new HashMap<>();

        frontier.add(startPos);
        cameFrom.put(startPos, null);

        //Loop through each possible available cell
        while(!frontier.isEmpty()){
            current = frontier.remove();    //maybe use poll if I just want to get a null

            //If current queue equals any of the center points.
            if (current == centerPoints[0] || current == centerPoints[1] || current == centerPoints[2] || current == centerPoints[3]){
                break;
            }

            for (Point next : getNeighbors(current)) {
                if(!cameFrom.containsKey(next)){    //If not already visited
                    frontier.add(next);
                    cameFrom.put(next, current);
                    System.out.println("neighbor!");

                }
            }
//            for next in graph.neighbors(current):
//            if next not in came_from:
//            frontier.put(next)
//            came_from[next] = current

        }



        //todo Add end point below.
        //enemyWayPoints.add(endPoint);
        return enemyWayPoints;
    }

    private ArrayList<Point> getNeighbors(Point current){
        ArrayList<Point> neighbors = new ArrayList<>();
        //Round current to the nearest point
        current.x = current.x - ((current.x - DefenceView.xGridStart) % DefenceView.tileWidth);
        current.y = current.y - ((current.y - DefenceView.yGridStart) % DefenceView.tileWidth);

        for (int y = 0; y < DefenceView.grid.length; y++) {
            for (int x = 0; x < DefenceView.grid[0].length; x++) {
                if(DefenceView.grid[y][x].x == current.x && DefenceView.grid[y][x].y == current.y){
                    //Get North neighbor
                    if(y-1 >= 0){
                        if(DefenceView.grid[y-1][x].available)
                            neighbors.add(new Point(DefenceView.grid[y-1][x].x, DefenceView.grid[y-1][x].y));
                    }
                    //Get East neighbor
                    if(x+1 < DefenceView.grid[0].length){
                        if(DefenceView.grid[y][x+1].available)
                            neighbors.add(new Point(DefenceView.grid[y][x+1].x, DefenceView.grid[y][x+1].y));
                    }
                    //Get South neighbor
                    if(y+1 < DefenceView.grid.length){
                        if(DefenceView.grid[y+1][x].available)
                            neighbors.add(new Point(DefenceView.grid[y+1][x].x, DefenceView.grid[y+1][x].y));
                    }
                    //Get West neighbor
                    if(x-1 >= 0){
                        if(DefenceView.grid[y][x-1].available)
                            neighbors.add(new Point(DefenceView.grid[y][x-1].x, DefenceView.grid[y][x-1].y));
                    }
                }
            }
        }

        return neighbors;
    }

    //Some test points.
//        enemyWayPoints.add(new Point(DefenceView.grid[3][10].x, DefenceView.grid[3][10].y));
//        enemyWayPoints.add(new Point(DefenceView.grid[6][12].x, DefenceView.grid[6][12].y));
//        enemyWayPoints.add(new Point(DefenceView.grid[14][4].x, DefenceView.grid[14][4].y));

}
