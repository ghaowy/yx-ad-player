package com.imprexion.adplay.main;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.imprexion.adplay.R;

public class RippleView extends View {

    private Paint mPaint;
    private ValueAnimator mValueAnimator;
    private float mRadiu = 0;
    private float mWidth;
    private float mHeight;
    private TypedArray mTypedArray;
    private final static String TAG = "SpreadAnimation";
    private Context mContext;
    private int mType;
    private Canvas mCanvas;
    private int mAlpha;

    public RippleView(Context context) {
        super(context);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.spreadColor));
        mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.spreadView);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;

        if (mValueAnimator == null) {
            mType = mTypedArray.getInt(R.styleable.spreadView_radiu, (int) mRadiu);
            mPaint = new Paint();
            mPaint.setColor(getResources().getColor(R.color.spreadColor));
            mValueAnimator = ValueAnimator.ofFloat(30, mWidth / 2);
            mValueAnimator.setDuration(2700);
            mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mRadiu = (float) animation.getAnimatedValue();
                    mAlpha = (int) (255 - mRadiu / (mWidth / 2) * 255);
//                    ALog.d(TAG, "mAlpha = " + mAlpha);
                    mPaint.setAlpha(mAlpha);
                    postInvalidate();
                }
            });
            switch (mType) {
                case 1:
                    mValueAnimator.start();
                    break;
                case 2:
                    mValueAnimator.setStartDelay(900);
                    mValueAnimator.start();
                    break;
                case 3:
                    mValueAnimator.setStartDelay(1800);
                    mValueAnimator.start();
                    break;
                case 4:
                    mPaint.setAlpha(255);
                    mRadiu = 30;
                    break;
                default:
                    break;
            }
        }
        drawCircle(mRadiu);
    }

    private void drawCircle(float radiu) {
        mCanvas.drawCircle(mWidth / 2, mHeight / 2, radiu, mPaint);
    }

    public void stopSpread(){
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
    }

    public void startSpread(){
        postInvalidate();
    }
}
