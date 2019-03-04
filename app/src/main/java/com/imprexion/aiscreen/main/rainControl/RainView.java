package com.imprexion.aiscreen.main.rainControl;

import android.content.Context;
import android.content.res.TypedArray;
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
import java.util.List;
import java.util.Random;

public class RainView extends View {

    private List<RainItem> mRainList = new ArrayList<>();
    private int mScreenWidth;
    private int mScreenHeight;
    private Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rain_3);
    private Paint mPaint;
    private static final String TAG = "RainView";

    public void setScale(boolean scale) {
        isScale = scale;
    }

    private boolean isScale;

    public void setRainSize(int rainSize) {
        mRainSize = rainSize;
    }

    private int mRainSize = 90;
    private Matrix mMatrix;


    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public RainView(Context context) {
        super(context);
    }

    public RainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.rainView);
//        isScale = typedArray.getBoolean(R.styleable.rainView_isScale,false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mScreenWidth = getMeasuredWidth();
        mScreenHeight = getMeasuredHeight();
        initData();
    }

    public void initData() {
        Log.d(TAG, "initData");
        Random random = new Random();
        mMatrix = new Matrix();
        mPaint = new Paint();
        mRainList.clear();
        for (int i = 0; i < mRainSize; i++) {
            RainItem rainItem = new RainItem();
            rainItem.bitmap = mBitmap;
            rainItem.x = random.nextInt(mScreenWidth);
            rainItem.y = random.nextInt(mScreenHeight + 30) - 30;
            if (i < mRainSize * 0.4) {
                rainItem.scaleX = (random.nextInt(70) + 120) / 100f;
            } else {
                rainItem.scaleX = (random.nextInt(50) + 200) / 100f;
            }
            mRainList.add(rainItem);
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "size=" + mRainSize + " isScale=" + isScale + " mBitmap=" + mBitmap.getGenerationId());
        if (mRainList.size() == 0) {
            return;
        }
        for (int i = 0; i < mRainSize; i++) {
            RainItem rainItem = mRainList.get(i);
            mMatrix.reset();
            if (isScale) {
                mMatrix.setScale(rainItem.scaleX, rainItem.scaleX);
            }
            mMatrix.postTranslate(rainItem.x, rainItem.y);
            canvas.drawBitmap(mBitmap, mMatrix, mPaint);
        }

    }
}
