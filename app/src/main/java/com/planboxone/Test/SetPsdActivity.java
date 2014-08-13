package com.planboxone.Test;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.planboxone.MyActivity.BaseActivity;
import com.planboxone.MyWidget.MySlipSwitch;
import com.planboxone.R;


public class SetPsdActivity extends BaseActivity {
    RelativeLayout rl_modify;
    Boolean isHas;//是否有密码
    Boolean isOpen;//是否打开密码开关
    MySlipSwitch btn_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpsd);
        getData();
        switchBtn();
        btn_switch.setOnSwitchListener(new MySlipSwitch.OnSwitchListener() {

            @Override
            public void onSwitched(boolean isSwitchOn) {
                // TODO Auto-generated method stub
                if (isSwitchOn) {
                    //Toast.makeText(MainActivity.this, "开关已经开启", 300).show();
                    Intent mIntent = new Intent();
                    mIntent.setClass(SetPsdActivity.this, OpenPsdActivity.class);
                    startActivity(mIntent);
                } //开关打开状态，本地密码打开
                else {
                    Intent mIntent = new Intent();
                    mIntent.setClass(SetPsdActivity.this, ClosePsdActivity.class);
                    startActivity(mIntent);
                    //Toast.makeText(MainActivity.this, "开关已经关闭", 300).show();
                }//开关关闭，本地密码关闭
            }
        });
        rl_modify = (RelativeLayout) findViewById(R.id.rl_modify_pass);
        rl_modify.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (isOpen.equals(false)) {
                    Toast.makeText(SetPsdActivity.this, "您还未设置密码，请先设置密码", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Intent mIntent = new Intent();
                    mIntent.setClass(SetPsdActivity.this, ModifyPassActivity.class);
                    startActivity(mIntent);
                }
            }
        });

    }

    private void switchBtn() {
        // TODO Auto-generated method stub
        if (isOpen.equals(true)) {
            btn_switch.setSwitchState(true);
        } else {
            btn_switch.setSwitchState(false);
        }
    }

    private void getData() {
        // TODO Auto-generated method stub
        final SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        isHas = sharedPreferences.getBoolean("isHas", false);
        isOpen = sharedPreferences.getBoolean("isOpen", false);


        btn_switch = (MySlipSwitch) findViewById(R.id.btn_switch);
        btn_switch.setImageResource(R.drawable.bkg_switch, R.drawable.bkg_switch, R.drawable.btn_slip);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // TODO Auto-generated method stub
        SetPsdActivity.this.finish();
        return super.onKeyDown(keyCode, event);
    }
}
