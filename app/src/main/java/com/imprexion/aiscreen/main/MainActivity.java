package com.imprexion.aiscreen.main;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.imprexion.aiscreen.R;
import com.imprexion.aiscreen.tools.Tools;
import com.plattysoft.leonids.ParticleSystem;
import com.plattysoft.leonids.modifiers.AlphaModifier;
import com.plattysoft.leonids.modifiers.ScaleModifier;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.iv_2)
    ImageView iv2;
    @BindView(R.id.iv_3)
    ImageView iv3;
    @BindView(R.id.iv_4)
    ImageView iv4;
    @BindView(R.id.iv_5)
    ImageView iv5;
    @BindView(R.id.iv_6)
    ImageView iv6;
    @BindView(R.id.rl_status)
    RelativeLayout rlStatus;
    @BindView(R.id.smoke_emiter)
    View smokeEmiter;
    private ParticleSystem mParticleSystem;
    private boolean isHoverExit = true;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        smokeEmiter.post(new Runnable() {
            @Override
            public void run() {
                startAnim();
            }
        });
        setOnHoverListener(iv1);
        setOnHoverListener(iv2);
        setOnHoverListener(iv3);
        setOnHoverListener(iv4);
        setOnHoverListener(iv5);
        setOnHoverListener(iv6);
    }

    private void setOnHoverListener(final View view) {
        view.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                int what = event.getAction();
                switch (what) {
                    case MotionEvent.ACTION_HOVER_ENTER: //鼠标进入view
                        toastHover();
                        isHoverExit = false;
//                        view.setAlpha(0.7f);
                        Drawable drawable = getResources().getDrawable(R.drawable.count_hands_1);
                        Drawable zoomDrawable = Tools.zoomDrawable(drawable, 100, 100);

                        ((ImageView)view).setScaleType(ImageView.ScaleType.CENTER);
                        ((ImageView)view).setImageDrawable(zoomDrawable);
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isHoverExit) {
                                    view.setForeground(getResources().getDrawable(R.drawable.count_hands_2));
                                }
                            }
                        }, 1000);
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isHoverExit) {
                                    view.setForeground(getResources().getDrawable(R.drawable.count_hands_3));
                                }
                            }
                        }, 2000);
                        break;
                    case MotionEvent.ACTION_HOVER_MOVE: //鼠标在view上
//                        System.out.println("bottom ACTION_HOVER_MOVE");
//                        toastHoverMove();
//                        iv1.setForeground(getResources().getDrawable(R.drawable.elephant));
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT: //鼠标离开view
                        toastHoverExit();
                        view.setForeground(null);
//                        view.setAlpha(1);
                        isHoverExit = true;
                        Log.d(TAG, "isHoverExit=" + isHoverExit);
                        break;
                }
                return false;
            }

        });
    }

    private void toastHover() {
        Toast.makeText(this, "hover", Toast.LENGTH_SHORT).show();
    }

    private void toastHoverMove() {
        Toast.makeText(this, "hoverMove", Toast.LENGTH_SHORT).show();
    }

    private void toastHoverExit() {
        Toast.makeText(this, "hoverExit", Toast.LENGTH_SHORT).show();
    }

    private void startAnim() {
        if (mParticleSystem == null) {
            mParticleSystem = new ParticleSystem(this, 20, R.drawable.smoke, 1000);
            mParticleSystem.setSpeedByComponentsRange(-0.025f, 0.025f, -0.06f, -0.08f)
                    .setAcceleration(0.0001f, -45)
                    .setInitialRotationRange(0, 360)
                    .addModifier(new AlphaModifier(255, 0, 0, 1000))
                    .addModifier(new ScaleModifier(0.4f, 0.9f, 0, 1000))
                    .emit(smokeEmiter, 20);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mParticleSystem != null) {
            mParticleSystem.stopEmitting();
            mParticleSystem = null;
        }
    }

}
