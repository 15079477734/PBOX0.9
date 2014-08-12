//欢迎界面
//判断是不是第一次启动，根据情况启动对应的activity;
//若是第一次启动，先加载GuideActivity，再加载mainactivity，若是第二次启动，直接加载mainactivity；
package com.planboxone.MyActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.planboxone.R;

public class SplashActivity extends Activity {
    public final static int MAIN_ACTIVITY=1000;
    public final static int GUIDE_ACTIVITY=1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Boolean mIsFirstEnter=IsFirstEnter(SplashActivity.this,SplashActivity.this.getClass().getName());

        if (mIsFirstEnter) {
            mHandler.sendEmptyMessageDelayed(GUIDE_ACTIVITY, 2000);//5000是延迟的时间5秒

        }
        else {
            mHandler.sendEmptyMessageDelayed(MAIN_ACTIVITY, 2000);
        }
    }
    //判断是不是第一次启动的函数
    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    private Boolean IsFirstEnter(Context context, String name) {
        // TODO Auto-generated method stub
        if (context==null||name==null||"".equalsIgnoreCase(name)){
            return false;
        }//显示引导界面
        String mResultString=context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(KEY_GUIDE_ACTIVITY, "");
        if (mResultString.equalsIgnoreCase("false")) {
            return false;
        }
        else {
            return true;//是第一次启动
        }


    }
    Handler mHandler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GUIDE_ACTIVITY:
                    Intent intent=new Intent();
                    intent.setClass(SplashActivity.this, GuideActivity.class);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                    break;
                case MAIN_ACTIVITY:
                    intent=new Intent();
                    intent.setClass(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();
                    break;

            }
            super.handleMessage(msg);
        };
    };
}
