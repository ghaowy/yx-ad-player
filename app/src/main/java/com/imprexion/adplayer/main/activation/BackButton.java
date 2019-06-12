package com.imprexion.adplayer.main.activation;

import android.app.Instrumentation;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imprexion.adplayer.R;
import com.imprexion.library.YxLog;
import com.imprexion.library.YxStatistics;
import com.imprexion.library.YxThread;
import com.imprexion.library.util.ContextUtils;

/**
 * @author hardy
 * @name yx-window-service
 * @class name：com.imprexion.service.window.module
 * @class describe: 全局通用的返回按钮
 * @time 2019/4/20 13:43
 * @change
 * @chang time
 * @class describe
 */
public class BackButton extends FloatView {
    private static final String TAG = BackButton.class.getSimpleName();

    public View getRootView() {
        return mRootView;
    }

    private View mRootView;
    private ImageView mIvHandIcon;
    private TextView mTvInfo;
    private MySurfaceView mMySurfaceView;

    private Instrumentation mInstrumentation;

    /**
     * 初始化按钮布局和参数
     */
    public BackButton() {

        setFloatViewLayout(R.layout.back_button_layout);

        // 获取屏幕宽高
        Resources resources = ContextUtils.get().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        YxLog.d(TAG, "BackButton --- density = " + density + ", width = " + width + ", height = " + height);

        // 距右边距4%，下边距15%
        getLayoutParams().x = width * 4 / 100;
        getLayoutParams().y = height * 10 / 100;
        YxLog.d(TAG, "BackButton ---  getLayoutParams().x = " + getLayoutParams().x + ", getLayoutParams().y = " + getLayoutParams().y);

        getLayoutParams().type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        // 设置悬浮窗口长宽数据
        getLayoutParams().width = WindowManager.LayoutParams.WRAP_CONTENT;
        getLayoutParams().height = WindowManager.LayoutParams.WRAP_CONTENT;
        getLayoutParams().gravity = Gravity.RIGHT | Gravity.BOTTOM;

        mRootView = getLayoutView().findViewById(R.id.rl_root);
        mTvInfo = getLayoutView().findViewById(R.id.tv_back_text);
        mIvHandIcon = getLayoutView().findViewById(R.id.iv_hand_icon);

        mMySurfaceView = new MySurfaceView();
        ((RelativeLayout) getLayoutView().findViewById(R.id.rl_layout)).addView(mMySurfaceView);

        mRootView.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                int what = event.getAction();
                switch (what) {
                    case MotionEvent.ACTION_HOVER_ENTER:
//                        YxLog.d(TAG, "onHover --- ACTION_HOVER_ENTER ");
                        // 鼠标进入view
                        mIvHandIcon.setVisibility(View.VISIBLE);
                        mMySurfaceView.setVisibility(View.INVISIBLE);
                        mTvInfo.setText(R.string.keep_dont_move);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT:
//                        YxLog.d(TAG, "onHover --- ACTION_HOVER_EXIT ");
                        // 鼠标离开view
                        mIvHandIcon.setVisibility(View.INVISIBLE);
                        mMySurfaceView.setVisibility(View.VISIBLE);
                        mTvInfo.setText(R.string.enter_launcher);
                        break;
                }
                return false;
            }
        });

        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                // 在单线程中执行
//                YxThread.single(new Runnable() {
//                    @Override
//                    public void run() {
////                        long start = System.currentTimeMillis();
//                        if (mInstrumentation == null) {
//                            mInstrumentation = new Instrumentation();
//                        }
//                        mInstrumentation.sendCharacterSync(KeyEvent.KEYCODE_BACK);
////                        YxLog.d(TAG, "onClick --- time = " + (System.currentTimeMillis() - start));
//                        YxLog.i(TAG, "user click back button");
//                    }
//                });
            }
        });
    }

    @Override
    public void showFloatView() {
        if (isShow() == false) {
        }
        super.showFloatView();

    }

    @Override
    public void refreshView() {
        super.refreshView();

    }

    @Override
    public void dismissWindow() {
        if (isShow() == true) {
        }
        super.dismissWindow();
    }

    /**
     * 这个类就是加工了SurfaceView之后的类，所有要运动的物件都最终放在这里进行绘制
     */
    class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
        private Canvas canvas;
        private SurfaceHolder surfaceHolder;
        private DrawThread mDrawThread;

        public MySurfaceView() {
            super(ContextUtils.get());
            this.surfaceHolder = this.getHolder();
            this.surfaceHolder.addCallback(this);

            // 使surfaceview放到最顶层
            setZOrderOnTop(true);
            // 使窗口支持透明度
            getHolder().setFormat(PixelFormat.TRANSLUCENT);
        }

        @Override
        public void surfaceCreated(SurfaceHolder arg0) {
            if (null == mDrawThread) {
                mDrawThread = new DrawThread();
                mDrawThread.start();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder arg0) {
            if (null != mDrawThread) {
                mDrawThread.stopThread();
                mDrawThread = null;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
            // 这里是SurfaceView发生变化的时候触发的部分
        }

        /**
         * 绘图线程
         */
        private class DrawThread extends Thread {
            // 画笔宽
            private final int PAINT_WIDTH = ViewUtils.dip2px(5);
            // 最大帧数 (1000 / 80)
            private final int DRAW_INTERVAL = ViewUtils.dip2px(50);
            // 第一个动画初始半径
            private final int INITAL_VALUE_ONE = ViewUtils.dip2px(6);
            // 第二个动画初始半径 比第一个延迟
            private final int INITAL_VALUE_TWO = ViewUtils.dip2px(-24);
            // 最大半径  决定动画周期
            private final int MAX_RADIUS = ViewUtils.dip2px(150);
            // 最大显示半径 决定显示大小
            private final int MAX_RADIUS_SHOW = ViewUtils.dip2px(50);
            // 半径增加的基础步长
            private final int STEP_BASE = ViewUtils.dip2px(2);//3;
            // 开始改变透明度的半径值
            private final int ALPHA_START_VALUE = ViewUtils.dip2px(25);
            // 透明度变化步长
            private final int ALPHA_STEP = 255 / (MAX_RADIUS_SHOW - ALPHA_START_VALUE);

            public boolean isRunning = false;

            // 圆心
            int verticalCenter = 0;
            int horizontalCenter = 0;
            // 半径
            int circleRadius = 0;
            int circleRadius2 = 0;
            // 画笔
            Paint paint;

            public DrawThread() {
                isRunning = true;

                verticalCenter = getHeight() / 2;
                horizontalCenter = getWidth() / 2;
                circleRadius = INITAL_VALUE_ONE;
                circleRadius2 = INITAL_VALUE_TWO;

                paint = new Paint();
                // 抗锯齿
                paint.setAntiAlias(true);
                paint.setColor(Color.WHITE);
                // 设置空心
                paint.setStyle(Paint.Style.STROKE);

                // 设置线宽
                paint.setStrokeWidth(PAINT_WIDTH);
                YxLog.d(TAG, "DrawThread --- PAINT_WIDTH = " + PAINT_WIDTH);
                YxLog.d(TAG, "DrawThread --- INITAL_VALUE_ONE = " + INITAL_VALUE_ONE);
                YxLog.d(TAG, "DrawThread --- INITAL_VALUE_TWO = " + INITAL_VALUE_TWO);
                YxLog.d(TAG, "DrawThread --- ALPHA_STEP = " + ALPHA_STEP);
                YxLog.d(TAG, "DrawThread --- STEP_BASE = " + STEP_BASE);

            }

            public void stopThread() {
                isRunning = false;
                boolean workIsNotFinish = true;
                while (workIsNotFinish) {
                    try {
                        // 保证run方法执行完毕
                        this.join();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    workIsNotFinish = false;
                }
            }

            public void run() {
                long deltaTime = 0;
                long tickTime = 0;
                tickTime = System.currentTimeMillis();

                long startTime = System.currentTimeMillis();
                // 测试一个动画周期时间
                long cycleTime = System.currentTimeMillis();
                float arg = 1;
                int step = 0;
                while (isRunning) {
                    Canvas canvas = null;
                    try {
                        synchronized (surfaceHolder) {
                            canvas = surfaceHolder.lockCanvas();

                            // 步长补充机制，保证画面流程
                            arg = (System.currentTimeMillis() - startTime) / (float) DRAW_INTERVAL;
                            startTime = System.currentTimeMillis();
                            step = (int) (STEP_BASE * arg) + STEP_BASE;

                            circleRadius += step;
                            circleRadius2 += step;

                            if (circleRadius >= MAX_RADIUS) {
                                circleRadius = INITAL_VALUE_ONE;
                                circleRadius2 = INITAL_VALUE_TWO;
//                                YxLog.d(TAG, "DrawThread --- System.currentTimeMillis() - cycleTime = "
//                                        + (System.currentTimeMillis() - cycleTime) + " --- arg = " + arg);
//                                cycleTime = System.currentTimeMillis();
                            }

                            drawSprite(canvas);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if ((null != surfaceHolder) && (null != canvas)) {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }

                    // 加睡眠，释放资源
                    deltaTime = System.currentTimeMillis() - tickTime;
                    tickTime = System.currentTimeMillis();
                    if (deltaTime < DRAW_INTERVAL) {
                        try {
                            Thread.sleep(DRAW_INTERVAL - deltaTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


            /**
             * 绘制动画
             *
             * @param canvas
             */
            private void drawSprite(Canvas canvas) {
                if (canvas == null) {
                    return;
                }

                // 清屏操作 绘制透明色
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                if ((circleRadius > INITAL_VALUE_ONE) && (circleRadius <= MAX_RADIUS_SHOW)) {
                    // 画笔透明度
                    if (circleRadius >= ALPHA_START_VALUE) {
                        paint.setAlpha(255 - ALPHA_STEP * (circleRadius - ALPHA_START_VALUE));
                    } else {
                        paint.setAlpha(255);
                    }

                    canvas.drawCircle(horizontalCenter, verticalCenter, circleRadius, paint);
                }

                if ((circleRadius2 > INITAL_VALUE_ONE) && (circleRadius2 <= MAX_RADIUS_SHOW)) {
                    // 画笔透明度
                    if (circleRadius2 >= ALPHA_START_VALUE) {
                        paint.setAlpha(255 - ALPHA_STEP * (circleRadius2 - ALPHA_START_VALUE));
                    } else {
                        paint.setAlpha(255);
                    }
                    canvas.drawCircle(horizontalCenter, verticalCenter, circleRadius2, paint);
                }
            }
        }
    }

}
