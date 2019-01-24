package com.imprexion.aiscreen.functionPart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.webkit.WebView;

import com.imprexion.aiscreen.R;
import com.imprexion.aiscreen.tools.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        Tools.setWebView(webView);
        webView.loadUrl(getIntent().getStringExtra("url"));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Tools.hideNavigationBarStatusBar(this, hasFocus);
    }
}
