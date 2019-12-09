package com.imprexion.adplayer.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.Stack;

/**
 * @author : yan
 * @date : 2019/11/27 16:28
 * @desc : 管理Activity工具类
 */
public class ActivityStackUtil implements Application.ActivityLifecycleCallbacks {
    private Stack<Activity> mActivityStack = new Stack<>();
    private boolean isVisible;

    private ActivityStackUtil() {
    }

    public void finishAllActivity() {
        if (mActivityStack == null || mActivityStack.empty()) {
            return;
        }
        while (!mActivityStack.empty()) {
            Activity activity = mActivityStack.pop();
            if (activity == null) {
                continue;
            }
            activity.finish();
        }
    }

    public boolean isMainActivityVisible() {
        return isVisible;
    }

    public static class Holder {
        public static ActivityStackUtil instance = new ActivityStackUtil();
    }

    public void init(Context context) {
        if (context instanceof Application) {
            ((Application) context).registerActivityLifecycleCallbacks(this);
        } else {
            ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(this);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (mActivityStack == null) {
            return;
        }
        mActivityStack.push(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        isVisible = true;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        isVisible = false;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (mActivityStack == null || mActivityStack.empty()) {
            return;
        }
        mActivityStack.remove(activity);
    }


    public void exit() {
        finishAllActivity();
    }
}
