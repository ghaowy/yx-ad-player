package com.imprexion.aiscreen.functionPart;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.imprexion.aiscreen.R;
import com.imprexion.aiscreen.tools.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView webView;

    private final static String TAG = "WebViewActivity";
    @BindView(R.id.fl_back)
    FrameLayout flBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        Tools.setWebView(webView);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                if (message.equals("webForParam")) {
                    String deviceId = Build.SERIAL;
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("deviceId", deviceId);
                        jsonObject.put("picUrl", "https://cn.bing.com/sa/simg/hpb/NorthMale_EN-US8782628354_1920x1080.jpg");
                        jsonObject.put("picText", "我跑来前端了哦~");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    result.confirm(jsonObject.toString());
                    Log.d(TAG, "webForParam=" + jsonObject.toString());
                    return true;
                }
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
        webView.loadUrl(getIntent().getStringExtra("url"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tools.hideNavigationBarStatusBar(this, true);
        flBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Tools.hideNavigationBarStatusBar(this, hasFocus);
    }
}
