package com.example.formtd;

import android.graphics.Point;

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
        System.out.println(width + "  " + height);
        createGrid();
    }

    //Create the size of the squares given the size constraints
    private void createGrid(){
        //Get numbers needed to fill grid
        //TODO make optimization to map width and start. If not divisible by xGridAmount (20), then create offset.
        int xMapWidth = deviceWidth - deviceWidth/20;   //total map width
        int xMapStart = deviceWidth/40;                 //Where the map starts (left margin)
        int yMapStart = deviceHeight/40;
        this.tileWidth = xMapWidth/xGridAmount;
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

//        //Print array
//        for (int y = 0; y < yGridAmount; y++) {
//            for (int x = 0; x < xGridAmount; x++) {
//                System.out.print("(" + grid[y][x].x + ", " + grid[y][x].y +  ") ");
//            }
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
