package com.example.formtd;

//This class is like an enum (except not final) and is to be used like a struct.
public class RectanglePoints {
    public int left;
    public int top;
    public int right;
    public int bottom;

    public RectanglePoints(){
        this.left = 0;
        this.top = 0;
        this.right = 0;
        this.bottom = 0;
    }

    public RectanglePoints(int left, int top, int right, int bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}
