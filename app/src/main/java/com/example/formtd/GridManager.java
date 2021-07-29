package com.example.formtd;

import android.graphics.Point;
import android.util.Log;

public class GridManager {
    private int deviceWidth;
    private int deviceHeight;
    private static final int xGridAmount = 20;  //20 WIDE GRID
    private static final int yGridAmount = 28;  //28 TALL GRID
    private int tileWidth;                      //This width applies to both x and y.
    private Point[][] grid;



    public GridManager(int width, int height){
        this.deviceWidth = width;
        this.deviceHeight = height;
        this.tileWidth = 0;
        //System.out.println(width + "  " + height);
        createGrid();
    }

    //Create the size of the squares given the size constraints
    private void createGrid(){
        //Get numbers needed to fill grid
        int xMapWidth = deviceWidth - deviceWidth/20;   //total map width
        this.tileWidth = xMapWidth/xGridAmount;
        int mapOffset = (xMapWidth - (tileWidth * xGridAmount))/2;      //Make extra space even on each side of screen

        int xMapStart = deviceWidth/40 + mapOffset;     //Where the map starts (left margin)
        int yMapStart = deviceHeight/40;
        int xPos = xMapStart;
        int yPos = yMapStart;

        //Now fill the grid array
        grid = new Point[yGridAmount][xGridAmount];
        for (int y = 0; y < yGridAmount; y++) {
            for (int x = 0; x < xGridAmount; x++) {
                grid[y][x] = new Point(xPos, yPos);
                xPos += tileWidth;
            }
            xPos = xMapStart;   //Reset x pos since it will repeat on each new line
            yPos += tileWidth;
        }

//        for (int y = 0; y < yGridAmount; y++) {       //test print
//            for (int x = 0; x < xGridAmount; x++)
//                System.out.print("(" + grid[y][x].x + ", " + grid[y][x].y +  ") ");
//            System.out.println();
//        }
    }

    public Point[][] getGrid(){
        return grid;
    }

    public int getTileWidth(){
        return tileWidth;
    }
}
