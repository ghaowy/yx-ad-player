package com.imprexion.aiscreen.main.rainControl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import java.util.List;
import java.util.Random;

public class RaindropView extends View {

    private final static int SIZE = 150;
    private List<RaindropItem> mRaindropItemList = new ArrayList<>();
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.raindrop_1);
    private boolean mIsRemoveLeft;
    private boolean mIsRemoveRight;
    private Random mRandom;
    private static final String TAG = "RaindropView";
    private int mValue = -1;


    public RaindropView(Context context) {
        super(context);
    }

    public RaindropView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mPaint = new Paint();
        initData();
    }

    private void initData() {
        mRandom = new Random();
        mRaindropItemList.clear();
        for (int i = 0; i < SIZE; i++) {
            RaindropItem raindropItem = new RaindropItem();
            raindropItem.x = mRandom.nextInt(mWidth);
            raindropItem.y = mRandom.nextInt(mHeight);
            if (i < SIZE * 0.95) {
                raindropItem.scaleX = (mRandom.nextInt(30) + 10) / 100f;
                raindropItem.offsetX = mRandom.nextInt(3) + 2;
            } else {
                raindropItem.scaleX = (mRandom.nextInt(30) + 60) / 100f;
                raindropItem.offsetX = mRandom.nextInt(3) + 6;
            }
            raindropItem.bitmap = mBitmap;
            mRaindropItemList.add(raindropItem);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "mIsRemoveLeft =" + mIsRemoveLeft + " mIsRemoveRight=" + mIsRemoveRight);
        Matrix matrix = new Matrix();
        for (int i = 0; i < SIZE; i++) {
            matrix.reset();
            RaindropItem raindropItem = mRaindropItemList.get(i);
            matrix.setScale(raindropItem.scaleX, raindropItem.scaleX);
            if (mIsRemoveLeft) {
                raindropItem.x = raindropItem.x - raindropItem.offsetX;
                matrix.postTranslate(raindropItem.x, raindropItem.y);
            } else if (mIsRemoveRight) {
                raindropItem.x = raindropItem.x + raindropItem.offsetX;
                matrix.postTranslate(raindropItem.x, raindropItem.y);
            } else {
                matrix.postTranslate(raindropItem.x, raindropItem.y);
            }
            canvas.drawBitmap(raindropItem.bitmap, matrix, mPaint);
        }
    }

    public void invalide() {
        initData();
        postInvalidate();
    }

    public void remove(boolean isRemoveLeft,boolean isRemoveRight) {
        mIsRemoveLeft = isRemoveLeft;
        mIsRemoveRight = isRemoveRight;
        mValue = -1;
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(null, "TranslationY", 0, 12);
        objectAnimator.setDuration(700);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = Integer.valueOf(animation.getAnimatedValue().toString());
                if (mValue != value) {
                    postInvalidate();
                    Log.d(TAG, "removeLeft invalide ,mvalue=" + value);
                }
                mValue = value;
            }
        });

        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsRemoveLeft = false;
            }
        });
        objectAnimator.start();
    }
}
