package com.example.formtd;

public class GridManager {
    private int deviceWidth;
    private int deviceHeight;


    public GridManager(int width, int height){
        this.deviceWidth = width;
        this.deviceHeight = height;
        System.out.println(width + "  " + height);
    }

    //Create the size of the squares given the size constraints
    private void createGrid(){
        //TODO use the offset from rectangle: canvas.drawRect(deviceWidth/40, deviceHeight/40,  deviceWidth*39/40, deviceHeight*3/4, paint);
    }
}
