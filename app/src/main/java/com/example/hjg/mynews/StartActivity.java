package com.example.hjg.mynews;

import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by HJG on 2017/6/27.
 */

public class StartActivity extends BaseActivity{
    @Override
    public int getLayoutRes() {
        return R.layout.activity_start;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        new Thread(){
            public void run(){
                SystemClock.sleep(1500);

                // 读取不到key为firstRun的值，则默认返回true，表示第一次启动应用
                boolean firstRun = SharedPreUtil.getBoolean(
                        getApplicationContext(),"firstRun",true);
                if (firstRun){
                    SharedPreUtil.saveBoolean(StartActivity.this,
                            "firstRun",false);
                    enterGuideActivity();
                }else {
                    enterMainActivity();
                }
            }

        }.start();
    }

    private void enterGuideActivity() {
        Intent intent = new Intent(this,GuideActivity.class);
        startActivity(intent);
        finish();
    }

    private void enterMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
