package com.example.formtd;

public class Grid {
    public int x;
    public int y;
    public boolean buildable;   //available to build on
    public boolean walkable;    //available to add a point on

    public Grid(){
        this.x = 0;
        this.y = 0;
        this.buildable = true;
        this.walkable = true;
    }

    public Grid(int x, int y){
        this.x = x;
        this.y = y;
        this.walkable = true;

    }


    public Grid(int x, int y, boolean available){
        this.x = x;
        this.y = y;
        this.buildable = true;
        this.walkable = true;
    }
}
