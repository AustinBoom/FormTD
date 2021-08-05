package com.example.formtd;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

//Manages creation and data of the grid.
public class GridManager {
    private int deviceWidth;
    private int deviceHeight;
    private static final int xGridAmount = 20;  //20 WIDE GRID
    private static final int yGridAmount = 28;  //30 TALL GRID
    private int margin = 50;                    //Since this is a denominator, the smaller the bigger the margin gets.
    private int tileWidth;                      //This width applies to both x and y.
    private Grid[][] grid;
    private int xMapStart;
    private int yMapStart;
    Paint paint;



    public GridManager(int width, int height){
        this.deviceWidth = width;
        this.deviceHeight = height;
        this.tileWidth = 0;
        this.xMapStart = 0;
        this.yMapStart = 0;
        paint = new Paint();
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
        grid = new Grid[yGridAmount][xGridAmount];
        for (int y = 0; y < yGridAmount; y++) {
            for (int x = 0; x < xGridAmount; x++) {
                grid[y][x] = new Grid(xPos, yPos);
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

    //Draws the grid background
    public void drawGrid(Canvas canvas){
        //Print array
        int left;
        int top;
        int right;
        int bottom;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                left = grid[y][x].x;
                right = left + tileWidth;
                top = grid[y][x].y;
                bottom = top + tileWidth;
                paint.setARGB(255, 155 - (6*((x+(y%2))%2)), 180 - (6*((x+(y%2))%2)), 255 - (6*((x+(y%2))%2))); //makes a checkeredboard
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }

        drawSpawnMidEndPoints(canvas);
    }

    //Draws the spawn point, midpoint, and endpoint. This is hard coded since it's part of the world.
    private void drawSpawnMidEndPoints(Canvas canvas){
        Paint paint = new Paint();
        paint.setARGB(255, 65, 5, 35);
        //Draw spawnpoint
        RectanglePoints corner = new RectanglePoints(grid[0][0].x, grid[0][0].y, grid[0][grid[0].length-1].x + tileWidth, grid[1][0].y);
        canvas.drawRoundRect(corner.left, corner.top, corner.right, corner.bottom, 30, 30, paint);
        //Draw endpoint
        corner.top = grid[grid.length-1][0].y;
        corner.bottom = grid[grid.length-1][0].y + tileWidth;
        canvas.drawRoundRect(corner.left, corner.top, corner.right, corner.bottom, 30, 30, paint);
        //Draw midpoint
        corner.left = grid[grid.length/2][grid[0].length/2 - 1].x;
        corner.top = grid[grid.length/2 - 1][grid[0].length/2].y;
        corner.right = corner.left + tileWidth * 2;
        corner.bottom = corner.top + tileWidth * 2;
        canvas.drawRoundRect(corner.left, corner.top, corner.right, corner.bottom, 30, 30, paint);

    }

    public Grid[][] getGrid(){
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
