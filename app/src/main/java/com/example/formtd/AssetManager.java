package com.example.formtd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//Holds all assets and has ability to rescale images.
public class AssetManager {
    Bitmap BUILD;
    Bitmap BUILDPRESSED;

    public AssetManager(Context context){
        BUILD = BitmapFactory.decodeResource(context.getResources(), R.drawable.build);
        BUILD = Bitmap.createScaledBitmap(BUILD, 250, 80, false);
        BUILDPRESSED = BitmapFactory.decodeResource(context.getResources(), R.drawable.buildpressed);
        BUILDPRESSED = Bitmap.createScaledBitmap(BUILDPRESSED, 200, 80, false);
    }

    //Rescale images given device dimensions
    public void rescale(int width, int height){

    }

}
