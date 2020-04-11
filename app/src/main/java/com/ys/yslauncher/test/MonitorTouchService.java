package com.ys.yslauncher.test;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.ys.yslauncher.WindowsUitlity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

public class MonitorTouchService extends AccessibilityService {
    private String TAG = "AliAccessibilityService";
    public static int time = 30*60*1000;
    private long mobile_time = 0;
    public static MonitorTouchService mService;
    private BackMain backMain;
    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x10:
                    long time1 = System.currentTimeMillis()-mobile_time ;
//                    Log.i(TAG,"time===="+System.currentTimeMillis()+"/"+mobile_time+"//"+time1);
                    if (time1>=time&&time1<2147483647)
                        WindowsUitlity.createScreenView(MonitorTouchService.this);
                    break;
            }
            handler.sendEmptyMessageDelayed(0x10,2000);
        }
    };
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "into onServiceConnected");
        mService = this;
        final IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.ys.SCREEN_OFF");
        registerReceiver(mBatInfoReceiver, filter);
        handler.sendEmptyMessage(0x10);
//        time = Integer.parseInt(getValueFromProp("persist.sys.ysketime"));
//        backMain = new BackMain(5 * 1000, 1000, this);
//        backMain.start();
//        startTimer();
    }

    BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
//            Log.d(TAG, "onReceive=======" + action);
            time = Integer.parseInt(getValueFromProp("persist.sys.screentime"));
        }
    };

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "into onAccessibilityEvent==>>"+event.getEventType());
        mobile_time = System.currentTimeMillis();
        Log.i(TAG,"time1="+mobile_time);
//        backMain.cancel();
//        backMain.start();
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt");
        mService = null;
//        backMain.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
//        mService = null;
//        backMain.cancel();
    }

    /**
     * 辅助功能是否启动
     */
    public  boolean isStart() {
        return mService != null;
    }


    public  String getValueFromProp(String key) {
        String value = "";
        try {
            Class<?> classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", new Class<?>[]{String.class});
            value = (String) getMethod.invoke(classType, new Object[]{key});
            if (value.equals(""))
                value = "5000";
        } catch (Exception e) {
        }
        return value;
    }

    public  void setValueToProp(String key, String val) {
        Class<?> classType;
        try {
            classType = Class.forName("android.os.SystemProperties");
            Method method = classType.getDeclaredMethod("set", new Class[]{String.class, String.class});
            method.invoke(classType, new Object[]{key, val});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
