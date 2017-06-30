package com.example.hjg.mynews;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by HJG on 2017/6/27.
 */

    public class GuideActivity extends BaseActivity{
    private ImageView iv01;
    private Button btnGo;
    private int count = 0;
    private MediaPlayer mMediaPlayer;

    private int[] imagesArray = new int[]{
            R.drawable.ad_new_version1_img1,
            R.drawable.ad_new_version1_img2,
            R.drawable.ad_new_version1_img3,
            R.drawable.ad_new_version1_img4,
            R.drawable.ad_new_version1_img5,
            R.drawable.ad_new_version1_img6,
            R.drawable.ad_new_version1_img7,
    };

    @Override
    public int getLayoutRes() {
        return R.layout.activity_guide;
    }

    private Handler mHandler = new Handler() {
        @Override   // mhandler发消息后，会执行此方法处理消息
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    startAnimation();
                    break;
            }
        }
    };


    @Override
    public void initView() {
        iv01 = (ImageView) findViewById(R.id.iv_01);
        btnGo = (Button) findViewById(R.id.btn_go);
    }

    @Override
    public void initListener() {
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterMainActivity();
            }
        });
    }

    private void enterMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void initData() {
        startAnimation();//显示动画
    }

    private void startAnimation() {
        count ++;
        count = count % imagesArray.length;//取余数
        iv01.setBackgroundResource(imagesArray[count]);

        iv01.setScaleX(1.0f);   // 控件恢复为原来的大小，1倍
        iv01.setScaleY(1.0f);

        iv01.animate()
                .scaleX(1.2f)       // 控件放大到原来的1.2倍
                .scaleY(1.2f)
                .setDuration(3500)      // 动画执行时间是3.5秒
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                            // 延迟1秒后发消息，发消息后，会调用mHandler的handleMessage方法， 此处what为0，handleMessage会根据0作判断。
                            mHandler.sendEmptyMessageDelayed(0,1000);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
    }

    private void playBackgroundMusic(){
        try {
            AssetFileDescriptor fileDescriptor = getAssets().openFd("new_version.mp3");

            mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),0L,fileDescriptor.getLength());
            mMediaPlayer.setLooping(true);      // 循环播放
            mMediaPlayer.setVolume(1.0f,1.0f);  // 左声道音量 右声道音量
            mMediaPlayer.prepare();     // 缓冲文件
            mMediaPlayer.start();       // 开始播放

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Activity界面显示时调用
    @Override
    protected void onStart() {
        super.onStart();
        //开始播放
        playBackgroundMusic();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 释放MediaPlayer资源
        if (mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
