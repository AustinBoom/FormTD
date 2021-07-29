package com.example.formtd;

import android.graphics.Point;
import android.util.Log;

import androidx.annotation.Nullable;

//Class when given the grid and location touch will return where to draw the highlighted section.
public class PlacementManager {
    private Point[][] grid;
    private int tileWidth;
    private boolean highlighted;
    private RectanglePoints currentRectangleHighlight;

    public PlacementManager(Point[][] grid, int tileWidth){
        this.grid = grid;
        this.tileWidth = tileWidth;
        this.highlighted = false;
        this.currentRectangleHighlight = new RectanglePoints();
    }

    public void turnOffHighlight(){
        highlighted = false;
    }


    public void setHighlightPlacement(int xTouch, int yTouch){
        highlighted = true;
        currentRectangleHighlight.left = 100;
        currentRectangleHighlight.top = 100;
        currentRectangleHighlight.right = 300;
        currentRectangleHighlight.bottom = 300;

        //put logic to find the coordinates here. Use the nearest point and then add two tile widths
        //in each direction to create the other points.
        for (int i = 0; i < grid[0].length - 1; i++) {  //TODO eventually do length-1 to account for the 2x2 shape.
            if(grid[0][i].x < xTouch){
                currentRectangleHighlight.left = grid[0][i].x;
                currentRectangleHighlight.right = currentRectangleHighlight.left + (tileWidth * 2);
            }
        }

        for (int i = 0; i < grid.length - 1; i++) {  //TODO eventually do length-1 to account for the 2x2 shape.
            if(grid[i][0].y < yTouch){
                currentRectangleHighlight.top = grid[i][0].y;
                currentRectangleHighlight.bottom = currentRectangleHighlight.top + (tileWidth * 2);
            }
        }


    }


    @Nullable       //May return null if no highlight to return
    public RectanglePoints getHighlightPlacement(){
        if(highlighted)
            return currentRectangleHighlight;
        return null;
    }
}

