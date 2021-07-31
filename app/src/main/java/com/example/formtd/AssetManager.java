package com.example.formtd;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimatedImageDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;

import static android.os.SystemClock.sleep;

//Holds all assets and has ability to rescale images.
public class AssetManager {
    private Handler handler = new Handler();
    Bitmap BUILD;
    Bitmap BUILDPRESSED;
    Context context;

    public AssetManager(Context context){
        BUILD = BitmapFactory.decodeResource(context.getResources(), R.drawable.build);
        BUILD = Bitmap.createScaledBitmap(BUILD, 250, 80, false);
        BUILDPRESSED = BitmapFactory.decodeResource(context.getResources(), R.drawable.buildpressed);
        BUILDPRESSED = Bitmap.createScaledBitmap(BUILDPRESSED, 250, 80, false);
        this.context = context;
    }


}
