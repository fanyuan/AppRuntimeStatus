package com.example.myprocstatus;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

public class LogUtil {
    private static final String TAG = "debug";
    public static void log(String msg){
        Log.d(TAG,msg);
    }
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info= context.getApplicationInfo();
            return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 只有在调试模式时打印日志
     * @param context
     * @param msg
     */
    public static void log(Context context,String msg){
        if(isApkDebugable(context)){
            Log.d("ddebug",msg);
        }
    }
}
