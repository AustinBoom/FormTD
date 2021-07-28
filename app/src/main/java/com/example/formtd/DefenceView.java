package com.example.formtd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DefenceView extends View implements View.OnTouchListener {
    //Boiler Plate
    Context context;

    //World
    int deviceWidth;
    int deviceHeight;

    public DefenceView(Context context) {
        super(context);
        this.context = context;
    }

    public DefenceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
    }


    //When screen is touched, respond!
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        invalidate();

        return false;
    }

    //Draw listener that updates the view.
    public void onDraw(Canvas canvas){

        //Just to test drawview
        Paint paint = new Paint();
        paint.setARGB(255, 155, 180, 255);
        canvas.drawRect(deviceWidth/40, deviceHeight/40,
                deviceWidth*39/40, deviceHeight*3/4, paint);
    }

    protected void onMeasure(int widthMeasure, int heightMeasure){
        deviceWidth = MeasureSpec.getSize(widthMeasure);
        deviceHeight = MeasureSpec.getSize(heightMeasure);

        System.out.println(deviceWidth + "  " + deviceHeight);

        //Boiler plate. Removing this is CATASTROPHIC!
        setMeasuredDimension(deviceWidth, deviceHeight);
    }
}