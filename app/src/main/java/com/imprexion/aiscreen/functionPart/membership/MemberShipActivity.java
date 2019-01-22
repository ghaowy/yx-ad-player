package com.imprexion.aiscreen.functionPart.membership;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.imprexion.aiscreen.R;
import com.imprexion.aiscreen.tools.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MemberShipActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_ship);
        ButterKnife.bind(this);
        Tools.setWebView(webView);
        webView.loadUrl(getIntent().getStringExtra("url"));
    }
}
