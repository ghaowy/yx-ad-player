package com.imprexion.adplay.tools;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;


public class IconFontTextView extends TextView {

//    public IconFontTextView(Context context) {
//        super(context);
//    }

    public IconFontTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IconFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        //加载字体文件
        Typeface typeface = IconFontTypeFace.getTypeface(context);
        this.setTypeface(typeface);
        //去掉padding,这样iconfont和普通字体容易对齐
        setIncludeFontPadding(false);
    }


    public static class IconFontTypeFace {

        //用static,整个app共用整个typeface就够了
        private static Typeface ttfTypeface = null;

        public static synchronized Typeface getTypeface(Context context) {
            if (ttfTypeface == null) {
                try {
                    ttfTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/iconfont.ttf");
                } catch (Exception e) {

                }
            }
            return ttfTypeface;
        }

        public static synchronized void clearTypeface() {
            ttfTypeface = null;
        }
    }
}

