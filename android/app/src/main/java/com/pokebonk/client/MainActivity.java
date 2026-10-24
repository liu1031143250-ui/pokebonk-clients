package com.pokebonk.client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * PokéBonk 全屏客户端：把 https://pokebonk.com 装进沉浸式横屏 WebView。
 * 没有浏览器地址栏/工具栏，切后台再回来游戏状态不丢。
 */
public class MainActivity extends Activity {

    private static final String GAME_HOST = "pokebonk.com";
    private static final String GAME_URL = "https://pokebonk.com/";

    private WebView gameView;
    private ProgressBar progressBar;
    private View errorPanel;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 玩游戏时屏幕常量
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        gameView = findViewById(R.id.game_view);
        progressBar = findViewById(R.id.progress);
        errorPanel = findViewById(R.id.error_panel);

        WebSettings s = gameView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);          // 游戏存档（localStorage）
        s.setDatabaseEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);
        s.setAllowFileAccess(false);
        s.setAllowContentAccess(false);

        gameView.setBackgroundColor(0xFF1A1630);
        gameView.setOverScrollMode(View.OVER_SCROLL_NEVER); // 去掉惯性滚动阴影
        gameView.setWebViewClient(new GameWebViewClient());
        gameView.setWebChromeClient(new GameChromeClient());

        findViewById(R.id.btn_retry).setOnClickListener(v -> {
            errorPanel.setVisibility(View.GONE);
            gameView.loadUrl(GAME_URL);
        });

        hideSystemBars();
        if (savedInstanceState == null) {
            gameView.loadUrl(GAME_URL);
        }
    }

    /** 沉浸式全屏：隐藏状态栏和导航栏，滑动只是临时唤出。 */
    private void hideSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.systemBars());
                controller.setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemBars();
        }
    }

    @Override
    public void onBackPressed() {
        // 有网页历史就先回退；没有则退到后台，避免误触直接杀游戏
        if (errorPanel.getVisibility() == View.VISIBLE) {
            errorPanel.setVisibility(View.GONE);
            return;
        }
        if (gameView.canGoBack()) {
            gameView.goBack();
            return;
        }
        moveTaskToBack(true);
    }

    private final class GameWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri uri = request.getUrl();
            String scheme = uri.getScheme();
            if ("http".equals(scheme) || "https".equals(scheme)) {
                if (GAME_HOST.equals(uri.getHost())) {
                    return false; // 站内页面，WebView 自己加载
                }
                openExternally(uri); // 外链交给系统浏览器
                return true;
            }
            openExternally(uri);
            return true;
        }

        private void openExternally(Uri uri) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } catch (Exception ignored) {
                // 没有能处理的 App 就忽略
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            errorPanel.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request,
                                    WebResourceError error) {
            if (request.isForMainFrame()) {
                progressBar.setVisibility(View.GONE);
                errorPanel.setVisibility(View.VISIBLE);
            }
        }
    }

    private final class GameChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress >= 100) {
                progressBar.setVisibility(View.GONE);
            } else {
                if (progressBar.getVisibility() != View.VISIBLE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                progressBar.setProgress(newProgress);
            }
        }
    }
}
