package com.planboxone.MyActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.planboxone.R;


public class SettingActivity extends BaseActivity {
    private final static String TAG = "MySettingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_my_setting);
        ((LinearLayout) findViewById(R.id.lv_passward)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((LinearLayout) findViewById(R.id.lv_reminder)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((LinearLayout) findViewById(R.id.lv_net)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((LinearLayout) findViewById(R.id.lv_backup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((LinearLayout) findViewById(R.id.lv_update)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((LinearLayout) findViewById(R.id.lv_share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent localIntent1 = new Intent("android.intent.action.SEND");
                localIntent1.setType("text/plain");
                localIntent1.putExtra("android.intent.extra.SUBJECT", "你好");
                localIntent1.putExtra("android.intent.extra.TEXT", "微计划是记录生活中重要的日子的小工具。还在为女友突然问你们相恋了多久而瞠目结舌吗？还在为关键时刻忘记女友的生日而发愁吗？那么这个小工具正是你需要的。\n\n安智市场:http://t.cn/aCtycJ\n应用汇:http://t.cn/aCtAuT\n魅族应用商店M9专版:http://t.cn/aCtMG8\n");
                startActivity(Intent.createChooser(localIntent1, "分享给好友"));
            }
        });
        ((LinearLayout) findViewById(R.id.lv_feedback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent localIntent2 = new Intent("android.intent.action.SENDTO");
                localIntent2.setData(Uri.parse("mailto:462679107.com"));
                localIntent2.putExtra("android.intent.extra.SUBJECT", "你好");
                startActivity(Intent.createChooser(localIntent2, ""));
            }
        });
    }




}
