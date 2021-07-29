package com.example.formtd;

import android.graphics.Point;
import android.util.Log;

import androidx.annotation.Nullable;

//Class when given the grid and location touch will return where to draw the highlighted section.
public class PlacementManager {
    //Data from GridManager.
    private Point[][] grid;
    private int tileWidth;
    private int xMapStart;
    private int yMapStart;

    //Data for placement functionality.
    private boolean highlighted;
    private RectanglePoints currentRectangleHighlight;

    public PlacementManager(Point[][] grid, int tileWidth, int xMapStart, int yMapStart){
        this.grid = grid;
        this.tileWidth = tileWidth;
        this.xMapStart = xMapStart;
        this.yMapStart = yMapStart;

        this.highlighted = false;
        this.currentRectangleHighlight = new RectanglePoints();
    }

    public void turnOffHighlight(){
        highlighted = false;
    }


    public void setHighlightPlacement(int xTouch, int yTouch){
        highlighted = true;

        int xOuterboundary = xMapStart + (grid[0].length * tileWidth);
        int yOuterboundary = yMapStart + (grid.length * tileWidth);

        //make sure touch is in bounds
        if(xTouch > xMapStart && yTouch > yMapStart && xTouch < xOuterboundary && yTouch < yOuterboundary) {
            //Round touch to the nearest grid point.
            for (int i = 0; i < grid[0].length - 1; i++) {      //Round x to nearest grid.
                if (grid[0][i].x < xTouch) {
                    currentRectangleHighlight.left = grid[0][i].x;
                    currentRectangleHighlight.right = currentRectangleHighlight.left + (tileWidth * 2);
                }
            }

            for (int i = 0; i < grid.length - 1; i++) {         //Round y to nearest grid.
                if (grid[i][0].y < yTouch) {
                    currentRectangleHighlight.top = grid[i][0].y;
                    currentRectangleHighlight.bottom = currentRectangleHighlight.top + (tileWidth * 2);
                }
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

