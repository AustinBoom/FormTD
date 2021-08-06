package com.example.formtd;

public class Grid {
    public int x;
    public int y;
    public boolean available;

    public Grid(){
        this.x = 0;
        this.y = 0;
        this.available = true;
    }

    public Grid(int x, int y){
        this.x = x;
        this.y = y;
    }


    public Grid(int x, int y, boolean available){
        this.x = x;
        this.y = y;
        this.available = available;
    }
}
