package com.example.myprocstatus;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.HashMap;

public class AppStatusHelper {
    public interface MsgHandler{
        /**
         * 状态消息处理方法
         * @param msgSummary
         * @param detailMsg
         */
        public void handlMsg(String msgSummary,String detailMsg);
    }

    /**
     * 进行初始化操作
     * @param application
     * @param msgHandler
     */
    public static void init(Application application,MsgHandler msgHandler){
        application.registerActivityLifecycleCallbacks(new AppStatusActivityLifecycleCallbacks(msgHandler));

        application.registerComponentCallbacks(new ComponentCallbacks2() {
            @Override
            public void onTrimMemory(int level) {
                if(level != ComponentCallbacks2.TRIM_MEMORY_COMPLETE ){
                    return;
                }

                String topActivity = AppStatusActivityLifecycleCallbacks.getTopActivity();
                String stack = AppStatusActivityLifecycleCallbacks.getActivitys().toString();
                String trimMemoryLevel = getTrimMemoryLevel(level);
                String type = "onTrimMemory";
                String msgSummary = "type:" + type + ";page:" + topActivity + ";trimMemoryLevel:" + trimMemoryLevel;

                HashMap<String,String> map = new HashMap<String, String>();
                map.put("type",type);
                map.put("top",topActivity);
                map.put("stack",stack);
                JSONObject jsonObject = new JSONObject(map);
                String detailMsg = jsonObject.toString();

                msgHandler.handlMsg(msgSummary,detailMsg);


                // 80   后台进程
                // 当前手机目前内存吃紧 （后台进程数量少），系统开始根据LRU缓存来清理进程，而该程序位于LRU缓存列表的最边缘位置，系统会先杀掉该进程，应尽释放一切可以释放的内存。
                // 官方文档：the process is nearing the end of the background LRU list, and if more memory isn't found soon it will be killed.
                //case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
            }

            private String getTrimMemoryLevel(int level){
                String result = "null";
                switch (level){
                    case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN :
                        result = "TRIM_MEMORY_UI_HIDDEN";
                        break;
                    case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
                        result = "TRIM_MEMORY_RUNNING_MODERATE";
                        break;
                    case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
                        result = "TRIM_MEMORY_RUNNING_LOW";
                        break;
                    case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                        result = "TRIM_MEMORY_RUNNING_CRITICAL";
                        break;
                    case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
                        result = "TRIM_MEMORY_BACKGROUND";
                        break;
                    case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
                        result = "TRIM_MEMORY_MODERATE";
                        break;
                    case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                        result = "TRIM_MEMORY_COMPLETE";
                        break;

                }
                return result;
            }

            @Override
            public void onLowMemory() {
                String topActivity = AppStatusActivityLifecycleCallbacks.getTopActivity();
                String stack = AppStatusActivityLifecycleCallbacks.getActivitys().toString();
                String type = "onLowMemory";
                String msgSummary = "type:" + type + ";page:" + topActivity ;

                HashMap<String,String> map = new HashMap<String, String>();
                map.put("type",type);
                map.put("top",topActivity);
                map.put("stack",stack);
                JSONObject jsonObject = new JSONObject(map);
                String detailMsg = jsonObject.toString();

                msgHandler.handlMsg(msgSummary,detailMsg);
            }

            @Override
            public void onConfigurationChanged(@NonNull Configuration newConfig) {}

        });
    }
}
