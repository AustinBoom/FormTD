package com.example.formtd;

import androidx.annotation.Nullable;

//Class when given the grid and location touch will return where to draw the highlighted section.
public class PlacementManager {
    //Data from GridManager.
    private Grid[][] grid;
    private int tileWidth;
    private int xMapStart;
    private int yMapStart;

    //Data for placement functionality.
    private boolean highlighted;
    private RectanglePoints currentRectangleHighlight;

    public PlacementManager(Grid[][] grid, int tileWidth, int xMapStart, int yMapStart){
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
                    //If touch leans slightly left, scoot over to the right one to make placement under finger
                    if(xTouch - grid[0][i].x < grid[0][i+1].x - xTouch && i > 0)
                        currentRectangleHighlight.left = grid[0][i-1].x;
                    else
                        currentRectangleHighlight.left = grid[0][i].x;

                    currentRectangleHighlight.right = currentRectangleHighlight.left + (tileWidth * 2);
                }
            }

            for (int i = 0; i < grid.length - 1; i++) {         //Round y to nearest grid.
                if (grid[i][0].y < yTouch) {
                    //If touch leans slightly down, scoot up one to make placement under finger
                    if(yTouch - grid[i][0].y < grid[i+1][0].y - yTouch && i > 0)
                        currentRectangleHighlight.top = grid[i-1][0].y;
                    else
                        currentRectangleHighlight.top = grid[i][0].y;

                    currentRectangleHighlight.bottom = currentRectangleHighlight.top + (tileWidth * 2);
                }
            }
        }
        else{
            turnOffHighlight();
        }
    }


    @Nullable       //May return null if no highlight to return
    public RectanglePoints getHighlightPlacement(){
        if(highlighted)
            return currentRectangleHighlight;
        return null;
    }
}

