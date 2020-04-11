package com.ys.yslauncher;

import android.app.Instrumentation;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;

import static com.ys.yslauncher.WindowsUitlity.createScreenView;
import static com.ys.yslauncher.WindowsUitlity.createSettingView;

public class MyService extends Service implements WindowsUitlity.OnStopService {
    private static final String TAG = "MyService";
    Handler handler = new Handler();
    long time = 0, time1 = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        WindowsUitlity.showPopupWindow(MyService.this);
//        final IntentFilter filter = new IntentFilter();
//        // 屏幕灭屏广播
//        filter.addAction(Intent.ACTION_SCREEN_OFF);
////        // 屏幕亮屏广播
////        filter.addAction(Intent.ACTION_SCREEN_ON);
////        // 屏幕解锁广播
////        filter.addAction(Intent.ACTION_USER_PRESENT);
//        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
//        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
//        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
//        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//        Log.d(TAG, "registerReceiver");
//        registerReceiver(mBatInfoReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void Stopservice() {

    }

    @Override
    public void Clear() {

    }


//    BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(final Context context, final Intent intent) {
//            String action = intent.getAction();
//            Log.d(TAG, "onReceive=======" + action);
//            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
//                time = System.currentTimeMillis();
//                if (time - time1<5000) {
////                    new Thread() {
////                        public void run() {
////                            try {
////                                TimeUnit.SECONDS.sleep(2);
////                            } catch (InterruptedException e) {
////                                e.printStackTrace();
////                            }
////                            try {
////                                Instrumentation inst = new Instrumentation();
////                                inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 360, 0));
////                                Log.e("Jamesa", ">>>>onCreateOptionsMenu");
////                                inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 360, 0));
////                            } catch (Exception e) {
////                                Log.e("Exception when sendPointerSync", e.toString());
////                            }
////                            Log.i(TAG,"Thread");
////                        }
////                    }.start();
//
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.i(TAG,"yuanhang");
//                            createScreenView(context);
//                        }
//                    },1000);
//
//                }
//                time1 = System.currentTimeMillis();
//            }
//        }
//    };
}
