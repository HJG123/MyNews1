package com.example.hjg.mynews;

import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.hjg.mynews.bean.NewsEntity;

/**
 * Created by HJG on 2017/6/30.
 */

public class NewsDetailActivity extends BaseActivity {
    private WebView webView;
    private ProgressBar progressBar;
    @Override
    public int getLayoutRes() {
        return R.layout.activity_detail;
    }

    @Override
    public void initView() {
        progressBar = (ProgressBar) findViewById(R.id.pb_01);
        //progressBar.setMax(100);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initWebView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //必须使用带主题的Activity
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.web_view);

        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);

        //禁止使用外部的浏览器打开网页
        webView.setWebViewClient(new WebViewClient(){
            @Override
            //网页加载完成
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                }else {
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        NewsEntity.ResultBean newsBean = (NewsEntity.ResultBean) getIntent()
                .getSerializableExtra("news");

        String newsUrl = newsBean.getUrl();
        webView.loadUrl(newsUrl);

    }
}
