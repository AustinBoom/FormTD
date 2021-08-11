package com.example.formtd;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class BreadthSearch {
    private ArrayList<Point> enemyWayPoints;
    private Point endPoint;
    private Point[] centerPoints;
    private Queue<Point> frontier;
    private HashMap<Point, Point> cameFrom;
    private Point current;
    private Point goal;

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
    }


    //Will calculate and return the path
    public ArrayList<Point> getUpToDatePath(Point startPos){  //todo get enemies current position

        //Initialize start conditions
        enemyWayPoints = new ArrayList<>();
        frontier = new LinkedList<>();
        cameFrom = new HashMap<>();
        goal = null;

        frontier.add(startPos);
        cameFrom.put(startPos, null);

        //If enemy is BEFORE the spawn, start the path to the start, then to a centerpoint.
        if(startPos.y < DefenceView.yGridStart){
            System.out.println(startPos + " " + DefenceView.yGridStart);
            enemyWayPoints.add(startPos);
            enemyWayPoints.add(centerPoints[1]);
            return enemyWayPoints;
        }
        //Loop through each possible available cell
        while(!frontier.isEmpty()){
            current = frontier.remove();    //maybe use poll if I just want to get a null

            //If current queue equals any of the center points, exit loop.
            //System.out.println(current.x + " " + centerPoints[0].x + " " + current.y + " "  + centerPoints[0].y);
            if ((current.x == centerPoints[0].x && current.y == centerPoints[0].y) || (current.x == centerPoints[1].x && current.y == centerPoints[1].y) || (current.x == centerPoints[2].x && current.y == centerPoints[2].y) || (current.x == centerPoints[3].x && current.y == centerPoints[3].y)){
                goal = current;
                break;
            }

            //Check each neighbor for availability and add to queue and hashmap.
            for (Point next : getNeighbors(current)) {
                if(!cameFrom.containsKey(next)){    //If not already visited
                    frontier.add(next);
                    cameFrom.put(next, current);
                }
            }
        }

        //Check if blocking
        if(goal == null){
            DefenceView.blocking = true;
            enemyWayPoints.add(new Point(centerPoints[0].x + DefenceView.tileWidth/2, centerPoints[0].y + DefenceView.tileWidth/2));
        }
        else {          //Trace back each step in the path to get the points.
            DefenceView.blocking = false;
            current = goal;
            while (current != startPos) {
                enemyWayPoints.add(current);
                current = cameFrom.get(current);
            }
            //enemyWayPoints.add(startPos); //NOTE: if path gets messed up, add this back.
            Collections.reverse(enemyWayPoints);
        }


        //todo reapply this algorithm again from center to end
        //enemyWayPoints.add(endPoint);
        if(enemyWayPoints.isEmpty()){
            System.out.println("IT GOT EMPTY?");
        }
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
                        if(DefenceView.grid[y-1][x].walkable) {
                            neighbors.add(new Point(DefenceView.grid[y - 1][x].x, DefenceView.grid[y - 1][x].y));
                        }
                    }
                    //Get East neighbor
                    if(x+1 < DefenceView.grid[0].length){
                        if(DefenceView.grid[y][x+1].walkable) {
                            neighbors.add(new Point(DefenceView.grid[y][x + 1].x, DefenceView.grid[y][x + 1].y));
                        }
                    }
                    //Get South neighbor
                    if(y+1 < DefenceView.grid.length){
                        if(DefenceView.grid[y+1][x].walkable) {
                            neighbors.add(new Point(DefenceView.grid[y + 1][x].x, DefenceView.grid[y + 1][x].y));
                        }
                    }
                    //Get West neighbor
                    if(x-1 >= 0){
                        if(DefenceView.grid[y][x-1].walkable) {
                            neighbors.add(new Point(DefenceView.grid[y][x - 1].x, DefenceView.grid[y][x - 1].y));
                        }
                    }
                }
            }
        }

        return neighbors;
    }

    //Some test points.
//        if (true) {
//        enemyWayPoints = new ArrayList<>();
//        enemyWayPoints.add(new Point(DefenceView.grid[3][10].x, DefenceView.grid[3][10].y));
//        enemyWayPoints.add(new Point(DefenceView.grid[6][12].x, DefenceView.grid[6][12].y));
//        enemyWayPoints.add(new Point(DefenceView.grid[14][4].x, DefenceView.grid[14][4].y));
//        return enemyWayPoints;
//       }

}
