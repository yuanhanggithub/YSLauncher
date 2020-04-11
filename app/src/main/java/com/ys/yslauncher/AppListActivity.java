package com.ys.yslauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AppListActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private List<AppInfo> appInfoList;
    private static final String TAG = "AppListActivity";
    private AppListAdapter appListAdapter;
    private UninstallPackageReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_INSTALL);
        filter.addAction(Intent.ACTION_MY_PACKAGE_REPLACED);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");
        receiver = new UninstallPackageReceiver();
        registerReceiver(receiver, filter);

        StatusBar statusBar = (StatusBar) findViewById(R.id.status_bar);
        statusBar.zoomed(false);

        GridView appListView = (GridView) findViewById(R.id.app_list);
        appListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        appInfoList = getAllProgramInfo();
        appListAdapter = new AppListAdapter();
        appListView.setOnItemClickListener(this);
        appListView.setOnItemLongClickListener(this);
        appListView.setAdapter(appListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        appInfoList = getAllProgramInfo();
        appListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (appInfoList == null) return;
        startActivity(appInfoList.get(position).packageName);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showUninstallDialog(position);
        return true;
    }

     private void showUninstallDialog(final int position) {
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        final AppInfo appInfo = appInfoList.get(position);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //    设置Title的图标
//        builder.setIcon(R.drawable.ic_launcher);
        //    设置Title的内容
        builder.setTitle("是否卸载该应用");
        //    设置Content来显示一个信息
        builder.setMessage("确定卸载吗？");
        //    设置一个PositiveButton
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (appInfo != null) {
                    Uri packageURI = Uri.parse("package:" + appInfo.packageName);
                    Intent intent = new Intent(Intent.ACTION_DELETE, packageURI);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            }
        });
        //    设置一个NegativeButton
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().dismiss();
            }
        });
        //    显示出该对话框
        builder.show();
    }

    private class UninstallPackageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction()) || Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
                appInfoList = getAllProgramInfo();
                appListAdapter.notifyDataSetChanged();
            }
        }
    }

    class AppListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return appInfoList.size();
        }

        @Override
        public AppInfo getItem(int position) {
            return appInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            AppInfo appInfo = getItem(position);

            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(AppListActivity.this);
                convertView = inflater.inflate(R.layout.app_list_item, null, false);
                holder.appText = (TextView) convertView.findViewById(R.id.name);
                holder.appText.setText(appInfo.appName);
                BitmapDrawable bd = (BitmapDrawable) appInfo.appIcon;
                Bitmap bitmap = getBitmap(bd.getBitmap());
                bitmap.setDensity(Bitmap.DENSITY_NONE);
                holder.appText.setCompoundDrawablesWithIntrinsicBounds(null, new BitmapDrawable(bitmap), null, null);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                holder.appText.setText(appInfo.appName);
                BitmapDrawable bd = (BitmapDrawable) appInfo.appIcon;
                Bitmap bitmap = getBitmap(bd.getBitmap());
                bitmap.setDensity(Bitmap.DENSITY_NONE);
                holder.appText.setCompoundDrawablesWithIntrinsicBounds(null, new BitmapDrawable(bitmap), null, null);
            }
            return convertView;
        }

        private Bitmap getBitmap(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

            float screenWidth = wm.getDefaultDisplay().getWidth();    // ???(??,?:480px)

            float scaleWidth = 0;
            Configuration newConfig = getResources().getConfiguration();
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (screenWidth > 960)
                    scaleWidth = screenWidth / 18 / width;
                else
                    scaleWidth = screenWidth / 15 / width;
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                scaleWidth = screenWidth / 11 / width;
            }

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);
            return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
    }


    static class ViewHolder {
        TextView appText;
    }

    private String[] apps = {"com.android.dialer", "com.adtv", "android.rk.RockVideoPlayer", "com.android.contacts", "com.android.gallery3d", "com.android.camera2", "com.android.music",
            "com.android.providers.downloads.ui", "com.android.settings", "com.android.rockchip", "com.android.browser"};

    List<AppInfo> getAllProgramInfo() {
        PackageManager pm = getPackageManager();
        List<AppInfo> appList = new ArrayList<AppInfo>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> packages = pm.queryIntentActivities(intent, 0);
        for (int i = 0; i < packages.size(); i++) {
            ResolveInfo packageInfo = packages.get(i);
            AppInfo tmpInfo = new AppInfo();
            tmpInfo.appName = packageInfo.loadLabel(pm).toString();
            tmpInfo.packageName = packageInfo.activityInfo.packageName;
            tmpInfo.appIcon = packageInfo.loadIcon(pm);
            Log.d(TAG, "packageName = " + tmpInfo.packageName);

            String temp = tmpInfo.packageName;
//            if ("com.android.dialer".equals(temp) || "com.android.contacts".equals(temp))
//                continue;

//			if("com.android.dialer".equals(temp) || "com.adtv".equals(temp) || "android.rk.RockVideoPlayer".equals(temp) || "com.android.contacts".equals(temp)
//                    ||"com.android.gallery3d".equals(temp) || "com.android.camera2".equals(temp) || "com.android.music".equals(temp) || "com.android.providers.downloads.ui".equals(temp)
//                    || "com.android.rockchip".equals(temp) || "com.android.browser".equals(temp) || "com.android.soundrecorder".equals(temp) || "com.android.documentsui".equals(temp))
//				continue;

//            if ("com.android.settings".equals(temp))
            appList.add(tmpInfo);
        }
        return appList;
    }

    private void startActivity(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null)
            Toast.makeText(this, R.string.activity_not_found, Toast.LENGTH_SHORT).show();
        else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
