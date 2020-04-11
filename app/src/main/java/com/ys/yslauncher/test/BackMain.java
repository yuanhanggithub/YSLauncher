package com.ys.yslauncher.test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;

import com.ys.yslauncher.WindowsUitlity;

public class BackMain extends CountDownTimer {
    private static final String TAG = "BackMain";
    private Context context;

    /**
     * 参数 millisInFuture       倒计时总时间（如60S，120s等）
     * 参数 countDownInterval    渐变时间（每次倒计1s）
     */
    public BackMain(long millisInFuture, long countDownInterval, Context context) {
        super(millisInFuture, countDownInterval);
        this.context = context;
    }

    // 计时完毕时触发
    @Override
    public void onFinish() {
        Log.d(TAG, "时间到。。。");
        WindowsUitlity.createScreenView(context);
        cancel();
    }

    // 计时过程显示
    @Override
    public void onTick(long millisUntilFinished) {
        //Log.d(TAG, "onTick==>>" + millisUntilFinished);
    }
}
