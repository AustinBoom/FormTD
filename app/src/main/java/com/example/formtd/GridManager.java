package com.example.formtd;

import android.graphics.Point;

public class GridManager {
    private int deviceWidth;
    private int deviceHeight;
    private static final int xGridAmount = 20;  //20 WIDE GRID
    private static final int yGridAmount = 30;  //30 TALL GRID
    private int margin = 50;                    //Since this is a denominator, the smaller the bigger the margin gets.
    private int tileWidth;                      //This width applies to both x and y.
    private Point[][] grid;
    private int xMapStart;
    private int yMapStart;



    public GridManager(int width, int height){
        this.deviceWidth = width;
        this.deviceHeight = height;
        this.tileWidth = 0;
        this.xMapStart = 0;
        this.yMapStart = 0;
        createGrid();
    }

    //Create the size of the squares given the size constraints
    private void createGrid(){
        //Get numbers needed to fill grid
        int xMapWidth = deviceWidth - deviceWidth/ margin;   //total map width
        this.tileWidth = xMapWidth/xGridAmount;
        int mapOffset = (xMapWidth - (tileWidth * xGridAmount))/2;      //Make extra space even on each side of screen

        this.xMapStart = deviceWidth/(margin *2) + mapOffset;     //Where the map starts (left margin)
        this.yMapStart = deviceHeight/40;
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

    public int getxMapStart() {
        return xMapStart;
    }

    public int getyMapStart() {
        return yMapStart;
    }
}
