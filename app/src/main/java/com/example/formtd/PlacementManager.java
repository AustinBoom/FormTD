package com.example.formtd;

//Manages existing towers and spot availability.
public class PlacementManager {
    Grid[][] grid;
    private int tileWidth;

    public PlacementManager(Grid[][] grid, int tileWidth) {
        this.grid = grid;
        this.tileWidth = tileWidth;
        //Initialize each spot
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                grid[y][x].buildable = true;
                grid[y][x].walkable = true;
            }
        }
        //Make the spawn, mid and endpoints unavailable spots.
        for (int x = 0; x < grid[0].length; x++) {
            grid[0][x].buildable = false;
            grid[grid.length-1][x].buildable = false;
        }
        grid[grid.length/2 - 1][grid[0].length/2 - 1].buildable = false;       //top left
        grid[grid.length/2 - 1][grid[0].length/2].buildable = false;           //top right
        grid[grid.length/2][grid[0].length/2 - 1].buildable = false;   //bottom left
        grid[grid.length/2][grid[0].length/2].buildable = false;       //bottom right


//        //test output
//        for (int y = 0; y < grid.length; y++) {
//            for (int x = 0; x < grid[0].length; x++) {
//                if (grid[y][x].buildable)
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
                    lefttop = grid[y][x].buildable;
                if(grid[y][x].x == left && grid[y][x].y == bottom)
                    leftbottom = grid[y][x].buildable;
                if(grid[y][x].x == right && grid[y][x].y == top)
                    righttop = grid[y][x].buildable;
                if(grid[y][x].x == right && grid[y][x].y == bottom)
                    rightbottom = grid[y][x].buildable;
            }
        }

        return lefttop & leftbottom & righttop & rightbottom;
    }

    //Same as one above bug takes RectanglePoints.
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
                    lefttop = grid[y][x].buildable;
                if(grid[y][x].x == left && grid[y][x].y == bottom)
                    leftbottom = grid[y][x].buildable;
                if(grid[y][x].x == right && grid[y][x].y == top)
                    righttop = grid[y][x].buildable;
                if(grid[y][x].x == right && grid[y][x].y == bottom)
                    rightbottom = grid[y][x].buildable;
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
                if(grid[y][x].x == left && grid[y][x].y == top) {
                    grid[y][x].buildable = false;
                    grid[y][x].walkable = false;
                }
                if(grid[y][x].x == left && grid[y][x].y == bottom) {
                    grid[y][x].buildable = false;
                    grid[y][x].walkable = false;
                }
                if(grid[y][x].x == right && grid[y][x].y == top) {
                    grid[y][x].buildable = false;
                    grid[y][x].walkable = false;
                }
                if(grid[y][x].x == right && grid[y][x].y == bottom) {
                    grid[y][x].buildable = false;
                    grid[y][x].walkable = false;
                }

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
                if(grid[y][x].x == left && grid[y][x].y == top) {
                    grid[y][x].buildable = true;
                    grid[y][x].walkable = true;
                }
                if(grid[y][x].x == left && grid[y][x].y == bottom) {
                    grid[y][x].buildable = true;
                    grid[y][x].walkable = true;
                }
                if(grid[y][x].x == right && grid[y][x].y == top) {
                    grid[y][x].buildable = true;
                    grid[y][x].walkable = true;
                }
                if(grid[y][x].x == right && grid[y][x].y == bottom) {
                    grid[y][x].buildable = true;
                    grid[y][x].walkable = true;
                }
            }
        }
    }
}
