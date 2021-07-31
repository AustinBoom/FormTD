package com.example.formtd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//Holds all assets and has ability to rescale images.
public class AssetManager {
    Bitmap BUILD;
    Bitmap BUILDPRESSED;

    public AssetManager(Context context){
        this.BUILD = BitmapFactory.decodeResource(context.getResources(), R.drawable.build);
        this.BUILDPRESSED = BitmapFactory.decodeResource(context.getResources(), R.drawable.buildpressed);
    }

    //Rescale images given device dimensions
    public void rescale(int width, int height){

    }

}
