package com.example.formtd;

//Manages existing towers and spot availability.
public class PlacementManager {
    private int tileWidth;

    public PlacementManager(Grid[][] grid, int tileWidth) {
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


    public boolean checkSpotAvailability(int left, int top){
        //Get index by going to coordinate
        boolean lefttop = false;
        boolean leftbottom = false;
        boolean righttop = false;
        boolean rightbottom = false;
        int right = left + tileWidth;
        int bottom = top + tileWidth;

        for (int y = 0; y < DefenceView.grid.length; y++) {
            for (int x = 0; x < DefenceView.grid[0].length; x++) {
                if(DefenceView.grid[y][x].x == left && DefenceView.grid[y][x].y == top)
                    lefttop = DefenceView.grid[y][x].buildable;
                if(DefenceView.grid[y][x].x == left && DefenceView.grid[y][x].y == bottom)
                    leftbottom = DefenceView.grid[y][x].buildable;
                if(DefenceView.grid[y][x].x == right && DefenceView.grid[y][x].y == top)
                    righttop = DefenceView.grid[y][x].buildable;
                if(DefenceView.grid[y][x].x == right && DefenceView.grid[y][x].y == bottom)
                    rightbottom = DefenceView.grid[y][x].buildable;
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

        for (int y = 0; y < DefenceView.grid.length; y++) {
            for (int x = 0; x < DefenceView.grid[0].length; x++) {
                if(DefenceView.grid[y][x].x == left && DefenceView.grid[y][x].y == top)
                    lefttop = DefenceView.grid[y][x].buildable;
                if(DefenceView.grid[y][x].x == left && DefenceView.grid[y][x].y == bottom)
                    leftbottom = DefenceView.grid[y][x].buildable;
                if(DefenceView.grid[y][x].x == right && DefenceView.grid[y][x].y == top)
                    righttop = DefenceView.grid[y][x].buildable;
                if(DefenceView.grid[y][x].x == right && DefenceView.grid[y][x].y == bottom)
                    rightbottom = DefenceView.grid[y][x].buildable;
            }
        }

        return lefttop & leftbottom & righttop & rightbottom;
    }

    public void placeTower(RectanglePoints rectanglePoints){
        int left = rectanglePoints.left;
        int top = rectanglePoints.top;
        int right = left + tileWidth;
        int bottom = top + tileWidth;

        for (int y = 0; y < DefenceView.grid.length; y++) {
            for (int x = 0; x < DefenceView.grid[0].length; x++) {
                if(DefenceView.grid[y][x].x == left && DefenceView.grid[y][x].y == top) {
                    DefenceView.grid[y][x].buildable = false;
                    DefenceView.grid[y][x].walkable = false;
                }
                if(DefenceView.grid[y][x].x == left && DefenceView.grid[y][x].y == bottom) {
                    DefenceView.grid[y][x].buildable = false;
                    DefenceView.grid[y][x].walkable = false;
                }
                if(DefenceView.grid[y][x].x == right && DefenceView.grid[y][x].y == top) {
                    DefenceView.grid[y][x].buildable = false;
                    DefenceView.grid[y][x].walkable = false;
                }
                if(DefenceView.grid[y][x].x == right && DefenceView.grid[y][x].y == bottom) {
                    DefenceView.grid[y][x].buildable = false;
                    DefenceView.grid[y][x].walkable = false;
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

        for (int y = 0; y < DefenceView.grid.length; y++) {
            for (int x = 0; x < DefenceView.grid[0].length; x++) {
                if(DefenceView.grid[y][x].x == left && DefenceView.grid[y][x].y == top) {
                    DefenceView.grid[y][x].buildable = true;
                    DefenceView.grid[y][x].walkable = true;
                }
                if(DefenceView.grid[y][x].x == left && DefenceView.grid[y][x].y == bottom) {
                    DefenceView.grid[y][x].buildable = true;
                    DefenceView.grid[y][x].walkable = true;
                }
                if(DefenceView.grid[y][x].x == right && DefenceView.grid[y][x].y == top) {
                    DefenceView.grid[y][x].buildable = true;
                    DefenceView.grid[y][x].walkable = true;
                }
                if(DefenceView.grid[y][x].x == right && DefenceView.grid[y][x].y == bottom) {
                    DefenceView.grid[y][x].buildable = true;
                    DefenceView.grid[y][x].walkable = true;
                }
            }
        }
    }
}
