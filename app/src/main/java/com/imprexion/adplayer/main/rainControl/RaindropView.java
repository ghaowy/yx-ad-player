package com.imprexion.adplayer.main.rainControl;

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
import com.imprexion.adplayer.tools.ALog;

import android.view.View;

import com.imprexion.adplayer.R;

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
    private Bitmap mBitmapAlpha = BitmapFactory.decodeResource(getResources(), R.drawable.raindrop);
    private boolean mIsRemoveLeft;
    private boolean mIsRemoveRight;
    private boolean mIsRemoveUp;
    private boolean mIsRemoveDown;
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
            if (i < SIZE * 0.65) {
                raindropItem.scaleX = (mRandom.nextInt(40) + 10) / 100f;
                raindropItem.offsetX = mRandom.nextInt(2) + 2;
                raindropItem.bitmap = mBitmapAlpha;
            } else if (i < SIZE * 0.95) {
                raindropItem.scaleX = (mRandom.nextInt(30) + 20) / 100f;
                raindropItem.offsetX = mRandom.nextInt(3) + 2;
                raindropItem.bitmap = mBitmap;
            } else {
                raindropItem.scaleX = (mRandom.nextInt(30) + 60) / 100f;
                raindropItem.offsetX = mRandom.nextInt(3) + 6;
                raindropItem.bitmap = mBitmap;
            }
//            raindropItem.bitmap = mBitmap;
            mRaindropItemList.add(raindropItem);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        ALog.d(TAG, "mIsRemoveLeft =" + mIsRemoveLeft + " mIsRemoveRight=" + mIsRemoveRight);
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
            } else if (mIsRemoveUp) {
                raindropItem.y = raindropItem.y - raindropItem.offsetX;
                matrix.postTranslate(raindropItem.x, raindropItem.y);
            } else if (mIsRemoveDown) {
                raindropItem.y = raindropItem.y + raindropItem.offsetX;
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

    public void remove(boolean isRemoveLeft, boolean isRemoveRight, boolean isRemoveUp, boolean isRemoveDown) {
        mIsRemoveLeft = isRemoveLeft;
        mIsRemoveRight = isRemoveRight;
        mIsRemoveDown = isRemoveDown;
        mIsRemoveUp = isRemoveUp;
        mValue = -1;
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(null, "TranslationY", 0, 12);
        objectAnimator.setDuration(500);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = Integer.valueOf(animation.getAnimatedValue().toString());
                if (mValue != value) {
                    postInvalidate();
                    ALog.d(TAG, "removeLeft invalide ,mvalue=" + value);
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
