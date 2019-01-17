package com.imprexion.aiscreen.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.imprexion.aiscreen.R;

import java.util.ArrayList;
import java.util.Random;

public class SnowWeather extends View {

    private ArrayList<ItemSnow> mItemSnows;
    private Paint mPaint;
    private Random mRandom;
    private Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable    .snow);
    private int mWidth;
    private int mHeight;
    private int mPicWidth = mBitmap.getWidth();
    private int mPicHeight = mBitmap.getHeight();
    private final static String TAG = "SnowView";
    private int snowOffset = 3;
    private boolean isRun;
    private Matrix mMatrix;


    public int getSnowOffset() {
        return snowOffset;
    }

    public void setSnowOffset(int snowOffset) {
        this.snowOffset = snowOffset;
        isRun = !isRun;
        postInvalidate();
    }

    public SnowWeather(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        initData();
    }

    public void initData() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mRandom = new Random();
        mItemSnows = new ArrayList<>();
        mMatrix = new Matrix();
        for (int i = 0; i < 80; i++) {
            ItemSnow itemSnow = new ItemSnow();
            itemSnow.x = mRandom.nextInt(mWidth);
            itemSnow.y = mRandom.nextInt(mHeight - 120);
            itemSnow.offsetX = mRandom.nextInt(4) - 2;
            itemSnow.scale = (mRandom.nextInt(60) + 70) / 100f;
            itemSnow.bitmap = mBitmap;
            mItemSnows.add(itemSnow);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw");
        super.onDraw(canvas);
//        initData();
        for (int i = 0; i < mItemSnows.size(); i++) {
            ItemSnow itemSnow = mItemSnows.get(i);
            mMatrix.reset();
            mMatrix.postScale(itemSnow.scale, itemSnow.scale);
            itemSnow.x = itemSnow.x + itemSnow.offsetX;
            mMatrix.postTranslate(itemSnow.x, itemSnow.y);
            canvas.drawBitmap(itemSnow.bitmap, mMatrix, mPaint);
//            ItemSnow itemSnow = mItemSnows.get(i);
//            itemSnow.bitmap = scaleBitmap(itemSnow.scale);
//            itemSnow.x = itemSnow.x + itemSnow.offsetX;
//            canvas.drawBitmap(itemSnow.bitmap, itemSnow.x, itemSnow.y, mPaint);
        }

//        ValueAnimator valueAnimator = null;
//        if (!isRun) {
//            valueAnimator = ValueAnimator.ofFloat(1,100);
//            valueAnimator.setDuration(1000);
//            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    postInvalidate();
//                }
//            });
//            isRun = true;
//            valueAnimator.start();
//        }
        if (isRun) {
            postInvalidate();
        }


    }

    private Bitmap scaleBitmap(float scale) {
        Matrix matrix = new Matrix();
        int scaleWidth = (int) (mPicWidth * scale);
        int scaleHeight = (int) (mPicHeight * scale);
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(mBitmap, 0, 0, mPicWidth, mPicHeight, matrix, true);
    }

    public class ItemSnow {

        public int x;
        public int y;
        public int offsetX;
        public int offsety;
        public float scale;
        public Bitmap bitmap;

    }
}
