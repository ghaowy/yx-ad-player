package com.imprexion.adplayer.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;

import com.imprexion.adplayer.R;
import com.imprexion.adplayer.app.Constants;
import com.imprexion.adplayer.bean.ADContentInfo;
import com.imprexion.adplayer.main.MainActivity;

import java.util.ArrayList;

/**
 * @author : yan
 * @date : 2019/11/27 18:09
 * @desc : TODO
 */
public class ActivityLaunchUtil {

    public static void launchMainActivity(Context context, ArrayList<ADContentInfo> dataList, boolean newUpdateFlag, boolean isVideo) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Constants.Key.KEY_MESSAGE_TYPE, "playNext");
        intent.putParcelableArrayListExtra(Constants.Key.KEY_DATA, dataList);
        intent.putExtra(Constants.Key.KEY_IS_NEW_DATA, newUpdateFlag);
        intent.putExtra(Constants.Key.KEY_IS_VIDEO, isVideo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.right_in, R.anim.left_out);
        context.startActivity(intent, options.toBundle());
    }
}
