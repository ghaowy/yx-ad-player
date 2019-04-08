package com.imprexion.aiscreen.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.imprexion.aiscreen.tools.ALog;
import android.view.View;

import com.imprexion.aiscreen.R;

import java.util.ArrayList;
import java.util.Random;

public class SnowWeather extends View {

    private ArrayList<ItemSnow> mItemSnows;
    private Paint mPaint;
    private Random mRandom;
    private Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.snow);
    private int mWidth;
    private int mHeight;
    private int mPicWidth = mBitmap.getWidth();
    private int mPicHeight = mBitmap.getHeight();
    private final static String TAG = "SnowWeather";
    private int snowOffset = 3;
    private boolean isRun = true;
    private Matrix mMatrix;
    private Canvas mCanvas;


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
        ALog.d(TAG, "onMeasure");
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
        for (int i = 0; i < 100; i++) {
            ItemSnow itemSnow = new ItemSnow();
            itemSnow.x = mRandom.nextInt(mWidth);
            itemSnow.y = mRandom.nextInt(mHeight - 120);
            itemSnow.offsetX = mRandom.nextInt(6) - 2;
            itemSnow.scale = (mRandom.nextInt(40) + 40) / 100f;
            itemSnow.bitmap = mBitmap;
            mItemSnows.add(itemSnow);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas = canvas;
//        ALog.d(TAG, "onDraw");
        super.onDraw(canvas);
        for (int i = 0; i < mItemSnows.size(); i++) {
            ItemSnow itemSnow = mItemSnows.get(i);
            mMatrix.reset();
            mMatrix.postScale(itemSnow.scale, itemSnow.scale);
            itemSnow.x = itemSnow.x + itemSnow.offsetX;
            mMatrix.postTranslate(itemSnow.x, itemSnow.y);
            canvas.drawBitmap(itemSnow.bitmap, mMatrix, mPaint);
        }
        if (isRun) {
            postInvalidate();
        }


    }

    public void clearCavans() {
//        Paint paint = new Paint();
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        mCanvas.drawPaint(paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
//        invalidate();
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
