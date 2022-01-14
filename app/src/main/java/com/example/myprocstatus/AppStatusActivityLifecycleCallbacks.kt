package com.example.myprocstatus

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import org.json.JSONObject

class AppStatusActivityLifecycleCallbacks(var msgHandler: AppStatusHelper.MsgHandler) : Application.ActivityLifecycleCallbacks {
    companion object{
        /**
         * 前台activity
         */
        @JvmStatic
        public var topActivity:String = "null"
        /**
         * activity栈情况
         */
        @JvmStatic
        public var activitys:ArrayList<String> = ArrayList()
    }

    fun setTopActivity(activity: Activity){
        topActivity = activity.javaClass.canonicalName
        Log.d("debug", "setTopActivity = $topActivity")
    }
    fun unSetTopActivity(activity: Activity){
        var name = activity.javaClass.canonicalName
        if (topActivity.equals(name)){
            topActivity = "null"
        }
        Log.d("debug", "unSetTopActivity = $topActivity")
    }

    fun addActivity(activity: Activity) {
        activitys.add(activity.javaClass.simpleName)
    }
    fun removeActivity(activity: Activity){
        activitys.remove(activity.javaClass.simpleName)
    }
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d("debug", "onActivityCreated---")
        addActivity(activity)

        val topActivity = topActivity
        val stack = activitys.toString()

        var type = "create"

        var msgSummary:String = "type:$type;page:${activity.javaClass.name}"
        var map:HashMap<String, String> = HashMap()

        map["type"] = type
        map.put("page", activity.javaClass.name)
        map["top"] = topActivity
        map["stack"] = stack
        var jsonObject:JSONObject = JSONObject(map as Map<*, *>)
        var detailMsg = jsonObject.toString()
        msgHandler.handlMsg(msgSummary, detailMsg)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityPreResumed(activity: Activity) {
    }
    override fun onActivityResumed(activity: Activity) {

        setTopActivity(activity)

//        val topActivity = topActivity
//        val stack = activitys.toString()
//
//        var type = "resumed"
//
//        var msgSummary:String = "type:$type;page:${activity.javaClass.name}"
//        var map:HashMap<String, String> = HashMap()
//
//        map["type"] = type
//        map.put("page", activity.javaClass.name)
//        map["top"] = topActivity
//        map["stack"] = stack
//        var jsonObject:JSONObject = JSONObject(map as Map<*, *>)
//        var detailMsg = jsonObject.toString()
//        msgHandler.handlMsg(msgSummary, detailMsg)
    }

    override fun onActivityPostResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
        unSetTopActivity(activity)
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        removeActivity(activity)
    }
}