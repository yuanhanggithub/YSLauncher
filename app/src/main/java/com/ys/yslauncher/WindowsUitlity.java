package com.ys.yslauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 悬浮窗工具类
 * created by Pumpkin at 17/3/28
 */
public class WindowsUitlity {
    private static String TAG = WindowsUitlity.class.getSimpleName();
    private static WindowManager mWindowManager = null;
    private static WindowManager.LayoutParams params;
    public static Boolean isShown = false;
    private static View mView = null;
    private static OnStopService linsterner;
    private static View settingView,screenView;
    private static RelativeLayout rel_view,rel_screen;
    private static Context mContext;
    private static int mobileX;
    private static int mobileY;
    static AlertDialog dialog,dialog1;
    private static int number = 0;
    /**
     * 显示弹出框
     *
     * @param context
     */
    @SuppressWarnings("WrongConstant")
    public static void showPopupWindow(final Context context) {
        if (isShown) {
            return;
        }
        isShown = true;
//        linsterner = linstener;
        // 获取WindowManager
        mContext = context;
        mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Log.i("yuanhang", "showPopupWindow");
        mView = setUpView(context);
        params = new WindowManager.LayoutParams();
        // 类型，系统提示以及它总是出现在应用程序窗口之上。
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT |
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        // 设置flag
        int flags = canTouchFlags;
        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mobileX = (int) SharedPreferencesUtils.getParam(context, "lastX", -1);
        mobileY = (int) SharedPreferencesUtils.getParam(context, "lastY", -1);
        if (mobileX != -1) {
            params.x = mobileX;
            params.y =  mobileY;
        } else {
            params.x = (int) SharedPreferencesUtils.getParam(context, "point_x", -1);
        }
        Log.i("yaunhang","params.x=="+params.x+"/params.y="+params.y);
        mWindowManager.addView(mView, params);
    }


    private static int canTouchFlags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

    private static int notTouchFlags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

    /**
     * 隐藏弹出框
     */
    public static void hidePopupWindow() {
        if (isShown && null != mView) {
            mWindowManager.removeView(mView);
            isShown = false;
        }
    }


    static ImageView imageView;

    private static View setUpView(final Context context) {
        Log.i("yuanhang", "setUpView");
        View view = LayoutInflater.from(context).inflate(R.layout.layout_popwindow,
                null);
        imageView = view.findViewById(R.id.image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sleep(mContext);
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog("修改密码前请输入旧输入密码！！！",1);
                return true;
            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            private float lastX; //上一次位置的X.Y坐标
            private float lastY;
            private float nowX;  //当前移动位置的X.Y坐标
            private float nowY;
            private float tranX; //悬浮窗移动位置的相对值
            private float tranY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                boolean ret = false;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 获取按下时的X，Y坐标
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        Log.i("yaunhang","lastX=="+lastX+"/lastY="+lastY);
//                        ret = true;

                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 获取移动时的X，Y坐标
                        nowX = event.getRawX();
                        nowY = event.getRawY();
                        // 计算XY坐标偏移量
                        tranX = nowX - lastX;
                        tranY = nowY - lastY;
                        params.x += tranX;
                        params.y += tranY;
                        SharedPreferencesUtils.setParam(context, "lastX", params.x);
                        SharedPreferencesUtils.setParam(context, "lastY", params.y);
                        //更新悬浮窗位置
                        mWindowManager.updateViewLayout(mView, params);
                        //记录当前坐标作为下一次计算的上一次移动的位置坐标
                        lastX = nowX;
                        lastY = nowY;
                        Log.i("yaunhang","==lastX=="+lastX+"/==lastY="+lastY+"params.x="+params.x+"/params.y="+params.y);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });

        return view;
    }

    private static void sleep(Context context) {
        Utils.Write2File(new File("/sys/devices/platform/backlight/backlight/backlight/bl_power"), "1");
        Utils.Write2File(new File("/sys/spk/spk"), "1");
        createSettingView(context);
//        Utils.Write2File(new File("/sys/devices/platform/display-subsystem/drm/card0/card0-HDMI-A-1/status"), "1");


    }


    public interface OnStopService {
        void Stopservice();

        void Clear();
    }

    public static void createSettingView(final Context context) {
        hidePopupWindow();
        params = new WindowManager.LayoutParams();
        mWindowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.format = WindowManager.LayoutParams.LAYOUT_CHANGED;
        params.flags = WindowManager.LayoutParams.FORMAT_CHANGED;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        settingView = LayoutInflater.from(context).inflate(R.layout.view_sleep, null);
        rel_view = settingView.findViewById(R.id.rel_view);
        rel_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupWindow(context);
                Utils.Write2File(new File("/sys/devices/platform/backlight/backlight/backlight/bl_power"), "0");
                Utils.Write2File(new File("/sys/spk/spk"), "0");
                if (null != settingView) {
                    mWindowManager.removeView(settingView);
                }
                showDialog("请输入密码",0);
            }
        });
        mWindowManager.addView(settingView, params);
        settingView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }


    private static void showDialog(String title, final int id) {
        final EditText et = new EditText(mContext);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(title)
                .setView(et)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                switch (id){
                                    case 0:
                                        if (et.getText().toString().trim().equals((String) SharedPreferencesUtils.getParam(mContext, "psd","1234")+"")) {
                                        } else {
                                            et.setText("");
                                            et.setHint("密码错误，请重新输入！！！");
                                            try {
                                                java.lang.reflect.Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                                field.setAccessible(true);
                                                field.set(dialog, false);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        break;
                                    case 1:
                                        if (et.getText().toString().trim().equals((String)  SharedPreferencesUtils.getParam(mContext, "psd","1234")+"")) {
                                            showDialog("请输入新密码！！！",2);
                                        } else {
                                            Toast.makeText(mContext, "密码错误，请重新输入！！！",Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case 2:
                                        String password = et.getText().toString().trim();
                                        SharedPreferencesUtils.setParam(mContext, "psd",password);
                                            Toast.makeText(mContext, "密码修改成功,新密码为+"+password+"！！！",Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                switch (id) {
                                    case 0:
                                        showDialog("取消后直接锁屏，是否取消，无操作后五秒钟后自动进行锁屏！！！");
                                        try {
                                            java.lang.reflect.Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                            field.setAccessible(true);
                                            field.set(dialog, true);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        startTimer();
                                        break;
                                    case 1:
                                        break;
                                }
                            }
                        });
         dialog1 = builder.create();
        dialog1.setCancelable(false);
        dialog1.getWindow().setType(
                (WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                dialog1.show();
            }
        });
    }


    private static void showDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(title)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                sleep(mContext);
                                cacelTimer();
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                showDialog("请输入密码",0);
                                cacelTimer();
                            }
                        });
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.getWindow().setType(
                (WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                dialog.show();
            }
        });
    }

    public static void createScreenView(final Context context) {
                number++;
                if (number<=1) {
                    params = new WindowManager.LayoutParams();
                    mWindowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
                    params.type = WindowManager.LayoutParams.TYPE_PHONE;
                    params.format = WindowManager.LayoutParams.LAYOUT_CHANGED;
                    params.flags = WindowManager.LayoutParams.FORMAT_CHANGED;
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    screenView = LayoutInflater.from(context).inflate(R.layout.view_screen, null);
                    rel_screen = screenView.findViewById(R.id.rel_screen);
                    rel_screen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mWindowManager.removeView(screenView);
                            number = 0;
                        }
                    });
                mWindowManager.addView(screenView, params);
                screenView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    Log.i(TAG, "number" + number);
                }
        Log.i(TAG, "========number" + number);
    }

    private static void startTimer() {
        cacelTimer();
        timer = new Timer(true);
        task = new MyTask();
        timer.schedule(task, 5 * 1000);
    }

    private static void cacelTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
        }
    }

    private static Timer timer;
    private static MyTask task;
    private static class MyTask extends TimerTask {
        @Override
        public void run() {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    sleep(mContext);
                    dialog.dismiss();
                }
            });

        }
    }
}
