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
        mActivityStack.push(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (mActivityStack == null || mActivityStack.empty()) {
            return;
        }
        int search = mActivityStack.search(activity);
        if (search != -1) {
            mActivityStack.remove(search);
        }
    }


    public void exit() {
        finishAllActivity();
        System.exit(0);
    }
}
