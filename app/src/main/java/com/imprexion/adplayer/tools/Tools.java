package com.imprexion.adplayer.tools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.imprexion.adplayer.R;
import com.imprexion.adplayer.base.ADPlayApplication;
import com.imprexion.adplayer.main.AdActivity;
import com.imprexion.library.logger.YxLogger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Tools {

    private static final String TAG = "Tools";

    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(null, newbmp);
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static void setWebView(WebView webView) {
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    public static void hideNavigationBarStatusBar(Activity activity, boolean hasFocus) {
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public static List<String> getFilesAllName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            YxLogger.e(TAG, "空目录");
            return null;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String[] splits = files[i].getAbsolutePath().split("/");
            s.add(splits[splits.length - 1]);
            YxLogger.d(TAG, "filename = " + s.get(i));
        }
        return s;
    }


    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static void showPicWithGlide(ImageView imageView, String url) {
//        YxLogger.d(TAG, "url=" + url);
        if (!AdActivity.AD_DEFAULT.equals(url)) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ad_default_2)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.ad_default_2);
            Glide.with(ADPlayApplication.getInstance().getApplicationContext())
                    .load(url)
                    .apply(requestOptions)
                    .into(imageView);
        } else {
            Glide.with(ADPlayApplication.getInstance().getApplicationContext())
                    .load(R.drawable.ad_default_2)
                    .into(imageView);
        }
    }


    public static String getCurrentDate(String pattern) {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
//        String pattern = "yyyy-MM-dd";//格式yy-MM-dd(年份取末两位) ;yyyy-MM-dd HH:mm:ss(格式可以自行取舍)
        String currentDate = new SimpleDateFormat(pattern, Locale.getDefault()).format(date);
        YxLogger.d(TAG, "currentDate=" + currentDate);
        return currentDate;
    }

    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
//            YxLogger.d(TAG, "versionName=" + packageInfo.versionName);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int dpToPx(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, context.getResources().getDisplayMetrics());
    }
}
