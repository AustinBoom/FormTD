package com.example.formtd;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

public class EnemyDrawable extends Drawable implements ValueAnimator.AnimatorUpdateListener {

    private Path mPath;
    private Paint mPaint;
    private ValueAnimator mAnimator;
    int y;

    public EnemyDrawable() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setARGB(255, 0, 0, 255);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);

        y = 500;
    }

    public void startAnimating() {
        Rect b = getBounds();
        mAnimator = ValueAnimator.ofInt(-b.bottom, b.bottom);
        mAnimator.setDuration(2000);
        //mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
       // canvas.drawCircle(500, y, 50, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animator) {
        mPath.reset();
        Rect b = getBounds();
        mPath.moveTo(b.left, b.bottom);
        mPath.quadTo((b.right-b.left)/2, (Integer) animator.getAnimatedValue(), b.right, b.bottom);
        y += 1;


        invalidateSelf();
    }
}
