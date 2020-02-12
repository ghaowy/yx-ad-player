package com.imprexion.adplayer.launcher;

import android.view.View;
import android.widget.ImageView;

import com.imprexion.adplayer.R;
import com.imprexion.adplayer.base.BaseActivity;
import com.imprexion.adplayer.bean.ADLaunchPicData;
import com.imprexion.adplayer.net.http.HttpADManager;
import com.imprexion.adplayer.player.PlayerModel;

public class LauncherActivity extends BaseActivity implements PlayerModel.onPlayerDataListener<ADLaunchPicData> {


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
        // 请求网络图片，并加载
        HttpADManager.getInstance().getLaunchPic(this);
        mIvPic.setImageResource(R.drawable.ad_default_2);
    }

    @Override
    public void onDataLoadSuccess(ADLaunchPicData data) {

    }

    @Override
    public void onDataLoadFailed(int code, String msg) {

    }
}
