package org.apache.cordova.jumper;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

public class WebViewActivity extends Activity {

    final static String TAG = "WebView";

    private WebView mWebView;
    private ActionBar mActionBar;
    private Button undo,exit;

    private String originalUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebView();
        initActionBar();
        mWebView.loadUrl(getIntent().getStringExtra("url"));
    }

    void initWebView(){
        mWebView = new WebView(getApplicationContext());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.setWebViewClient(new CustomClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        setContentView(mWebView);
    }

    void initActionBar(){
        mActionBar = getActionBar();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout mLinearLayout = new LinearLayout(getApplicationContext());
        undo = new Button(getApplicationContext());
        undo.setText("<");
        undo.setTextColor(Color.WHITE);
        undo.setTextSize(24);
        undo.setBackgroundColor(Color.TRANSPARENT);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        exit = new Button(getApplicationContext());
        exit.setText("X");
        exit.setTextColor(Color.WHITE);
        exit.setTextSize(20);
        exit.setBackgroundColor(Color.TRANSPARENT);
        exit.setVisibility(View.INVISIBLE);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLinearLayout.addView(undo,mLayoutParams);
        mLinearLayout.addView(exit,mLayoutParams);

        mActionBar.setCustomView(mLinearLayout);
    }

    class CustomClient extends WebViewClient{
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d(TAG,"start"+url);
            Log.d(TAG,"originalUrl"+originalUrl);
            if(!originalUrl.equals("") && !originalUrl.equals(url)){
                exit.setVisibility(View.VISIBLE);
                undo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mWebView.goBack();
                    }
                });
            }else{
                exit.setVisibility(View.INVISIBLE);
                undo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG,"finish"+url);
            if(originalUrl.equals("")){
                originalUrl = url;
            }
        }
    }
}
