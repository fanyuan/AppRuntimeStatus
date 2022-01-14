package com.example.myprocstatus;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Application生命周期观察，提供整个应用进程的生命周期
 *
 * Lifecycle.Event.ON_CREATE只会分发一次，Lifecycle.Event.ON_DESTROY不会被分发。
 *
 * 第一个Activity进入时，ProcessLifecycleOwner将分派Lifecycle.Event.ON_START, Lifecycle.Event.ON_RESUME。
 * 而Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP，将在最后一个Activit退出后后延迟分发。如果由于配置更改而销毁并重新创建活动，则此延迟足以保证ProcessLifecycleOwner不会发送任何事件。
 *
 * 作用：监听应用程序进入前台或后台
 */
class ApplicationLifecycleObserver implements LifecycleObserver {
    private static final String TAG = "AppLifecycleObserver";

    private AppStatusHelper.MsgHandler msgHandler;
    public ApplicationLifecycleObserver(AppStatusHelper.MsgHandler handler){
        this.msgHandler = handler;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForeground() {
        //Log.w(TAG, "ApplicationObserver: app moved to foreground");
        LogUtil.log("应用进入前台");
        String type = "appForeground";

        String msgSummary = "type:" + type;
        HashMap<String, String> map = new HashMap();

        map.put("type", type);
        map.put("stack", AppStatusActivityLifecycleCallbacks.getActivitys().toString());
        JSONObject jsonObject = new JSONObject(map);
        String detailMsg = jsonObject.toString();

        //LogUtil.log("msgSummary = " + msgSummary + " --- detailMsg = " + detailMsg);
        msgHandler.handlMsg(msgSummary,detailMsg);
    }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        private void onAppBackground() {
            Log.w(TAG, "ApplicationObserver: app moved to background");
            LogUtil.log("应用退入后台");
            String type = "appBackground";

            String msgSummary = "type:"+type;
            HashMap<String, String> map =new HashMap();

            map.put("type",type);
            map.put("stack",AppStatusActivityLifecycleCallbacks.getActivitys().toString());
            JSONObject jsonObject = new JSONObject(map);
            String detailMsg = jsonObject.toString();

            //LogUtil.log("msgSummary = " + msgSummary + " --- detailMsg = " + detailMsg);
            msgHandler.handlMsg(msgSummary,detailMsg);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        private void onAppDestory(){
            LogUtil.log("应用Destory");
        }
}

