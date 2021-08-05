package com.example.formtd;

//Manages existing towers and spot availability.
public class PlacementManager {
    Grid[][] grid;
    private int tileWidth;

    public PlacementManager(Grid[][] grid, int tileWidth) {
        this.grid = grid;
        this.tileWidth = tileWidth;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                grid[y][x].available = true;
            }
        }
        //Make the spawn, mid and endpoints unavailable spots.
        for (int x = 0; x < grid[0].length; x++) {
            grid[0][x].available = false;
            grid[grid.length-1][x].available = false;
        }
        grid[grid.length/2 - 1][grid[0].length/2 - 1].available = false;       //top left
        grid[grid.length/2 - 1][grid[0].length/2].available = false;           //top right
        grid[grid.length/2][grid[0].length/2 - 1].available = false;   //bottom left
        grid[grid.length/2][grid[0].length/2].available = false;       //bottom right


//        //test output
//        for (int y = 0; y < grid.length; y++) {
//            for (int x = 0; x < grid[0].length; x++) {
//                if (grid[y][x].available)
//                    System.out.print("T ");
//                else
//                    System.out.print("F ");
//            }
//            System.out.println();
//        }
    }

    public Grid[][] updateGrid(){
        return grid;
    }

    public boolean checkSpotAvailability(int left, int top){
        //Get index by going to coordinate
        boolean lefttop = false;
        boolean leftbottom = false;
        boolean righttop = false;
        boolean rightbottom = false;
        int right = left + tileWidth;
        int bottom = top + tileWidth;

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if(grid[y][x].x == left && grid[y][x].y == top)
                    lefttop = grid[y][x].available;
                if(grid[y][x].x == left && grid[y][x].y == bottom)
                    leftbottom = grid[y][x].available;
                if(grid[y][x].x == right && grid[y][x].y == top)
                    righttop = grid[y][x].available;
                if(grid[y][x].x == right && grid[y][x].y == bottom)
                    rightbottom = grid[y][x].available;
            }
        }

        return lefttop & leftbottom & righttop & rightbottom;
    }

    public boolean checkSpotAvailability(RectanglePoints rectanglePoints){
        //Get index by going to coordinate
        boolean lefttop = false;
        boolean leftbottom = false;
        boolean righttop = false;
        boolean rightbottom = false;
        int left = rectanglePoints.left;
        int top = rectanglePoints.top;
        int right = left + tileWidth;
        int bottom = top + tileWidth;

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if(grid[y][x].x == left && grid[y][x].y == top)
                    lefttop = grid[y][x].available;
                if(grid[y][x].x == left && grid[y][x].y == bottom)
                    leftbottom = grid[y][x].available;
                if(grid[y][x].x == right && grid[y][x].y == top)
                    righttop = grid[y][x].available;
                if(grid[y][x].x == right && grid[y][x].y == bottom)
                    rightbottom = grid[y][x].available;
            }
        }

        return lefttop & leftbottom & righttop & rightbottom;
    }

    public void placeTower(RectanglePoints rectanglePoints){
        int left = rectanglePoints.left;
        int top = rectanglePoints.top;
        int right = left + tileWidth;
        int bottom = top + tileWidth;

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if(grid[y][x].x == left && grid[y][x].y == top)
                    grid[y][x].available = false;
                if(grid[y][x].x == left && grid[y][x].y == bottom)
                    grid[y][x].available  = false;
                if(grid[y][x].x == right && grid[y][x].y == top)
                    grid[y][x].available  = false;
                if(grid[y][x].x == right && grid[y][x].y == bottom)
                    grid[y][x].available = false;
            }
        }
    }

    //todo needs to be tested.
    public void removeTower(RectanglePoints rectanglePoints){
        int left = rectanglePoints.left;
        int top = rectanglePoints.top;
        int right = left + tileWidth;
        int bottom = top + tileWidth;

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if(grid[y][x].x == left && grid[y][x].y == top)
                    grid[y][x].available = true;
                if(grid[y][x].x == left && grid[y][x].y == bottom)
                    grid[y][x].available  = true;
                if(grid[y][x].x == right && grid[y][x].y == top)
                    grid[y][x].available  = true;
                if(grid[y][x].x == right && grid[y][x].y == bottom)
                    grid[y][x].available = true;
            }
        }
    }
}