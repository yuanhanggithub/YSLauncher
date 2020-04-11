package com.ys.yslauncher;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2018/3/26.
 */

public class Utils {
    private static final String TAG = "Utils";
    public static void startActivitySafely(Intent intent, Context context) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.activity_not_found, Toast.LENGTH_SHORT).show();
            Log.e(TAG,  " intent=" + intent, e);
        } catch (SecurityException e) {
            Toast.makeText(context, R.string.activity_not_found, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Launcher does not have the permission to launch " + intent +
                    ". Make sure to create a MAIN intent-filter for the corresponding activity " +
                    "or use the exported attribute for this activity. "
                     + " intent=" + intent, e);
        }
    }

    public static void Write2File(File file, String mode) {
        //Log.d(TAG,"Write2File,write mode = "+mode);
        if ((file == null) || (!file.exists()) || (mode == null)) return;

        try {
            FileOutputStream fout = new FileOutputStream(file);
            PrintWriter pWriter = new PrintWriter(fout);
            pWriter.println(mode);
            pWriter.flush();
            pWriter.close();
            fout.close();
        } catch (IOException re) {
            return;
        }
    }
}
