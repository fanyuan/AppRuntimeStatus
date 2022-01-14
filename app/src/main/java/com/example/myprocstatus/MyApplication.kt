package com.example.myprocstatus

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("debug","MyApplication --- onCreate")

        var handler = AppStatusHelper.MsgHandler { msgSummary, detailMsg ->
            var s = "abc"
            var str = s+"abc"
            LogUtil.log(this,"MyApplication msgSummary =  $msgSummary detailMsg = $detailMsg")
         }
        //MyApplicationUtil.init(this)
        AppStatusHelper.init(this,handler)

        //注册App生命周期观察者
        ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationLifecycleObserver(handler))
    }
}