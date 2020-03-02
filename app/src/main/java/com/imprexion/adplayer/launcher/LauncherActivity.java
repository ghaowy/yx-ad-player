package com.imprexion.adplayer.launcher;

import android.text.TextUtils;
import android.widget.ImageView;

import com.imprexion.adplayer.R;
import com.imprexion.adplayer.base.BaseActivity;
import com.imprexion.adplayer.bean.ADLaunchPicData;
import com.imprexion.adplayer.net.http.HttpADManager;
import com.imprexion.adplayer.player.PlayerModel;
import com.imprexion.library.YxImage;
import com.imprexion.library.YxLog;

public class LauncherActivity extends BaseActivity implements PlayerModel.onPlayerDataListener<ADLaunchPicData> {
    private static final String TAG = "LauncherActivity ";
    private boolean mIsDataLoaded;


    private ImageView mIvPic;

    @Override
    public int attachResId() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void initView() {
        mIvPic = findViewById(R.id.iv_pic);
    }

    @Override
    protected void initData() {
        if (mIsDataLoaded) {
            return;
        }
        mIsDataLoaded = true;
        // 请求网络图片，并加载
        HttpADManager.getInstance().getLaunchPic(this);
        mIvPic.setImageResource(R.drawable.ad_default_2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onDataLoadSuccess(ADLaunchPicData data) {
        YxLog.d(TAG, "onDataLoadSuccess--> " + data);
        if (mIvPic != null && !TextUtils.isEmpty(data.getFileUrl())) {
            YxImage.load(data.getFileUrl(), mIvPic, R.drawable.ad_default_2);
            mIsDataLoaded = true;
        }
        mIsDataLoaded = false;
    }

    @Override
    public void onDataLoadFailed(int code, String msg) {
        YxLog.d(TAG, "onDataLoadFailed--> " + msg);
        mIsDataLoaded = false;
    }
}
