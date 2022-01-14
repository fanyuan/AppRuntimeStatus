package com.example.myprocstatus

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.os.Process
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi

class ProcActivity : AppCompatActivity() {
    lateinit var tv:TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proc)
        tv = findViewById(R.id.tv)
        ByteUtil.b2m(Runtime.getRuntime().maxMemory())
        getStatus()
        Thread(){
            getProcStatus()
            var  cpuUsage = FileUtil.getProcessCpuUsage(Process.myPid().toString())
            Log.d("ddebug","cpuUsage = $cpuUsage")

            FileUtil.getProcessStatus(Process.myPid().toString())
            //FileUtil.getProcessSmaps(Process.myPid().toString())
            FileUtil.getProcessLimits(Process.myPid().toString())
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                FileUtil.getProcessFd(Process.myPid().toString())
//            }

            getThreads()
            LogUtil.log("-----------------------------------------------------------------")
        }.start()

        //getProcStatus()

    }
    fun getThreads(){
        var map = Thread.getAllStackTraces()
        Log.d("ddebug","map = $map")
    }
    private fun getProcStatus() {
        var pid = Process.myPid()
        var path = "/proc/$pid/status"
        Log.d("ddebug","path = $path")
        var str = FileUtil.readFileSdcardFile(path)
        var str1 = FileUtil.readSDFile(path)
        //tv.append(str)
        LogUtil.log("getProcStatus --- str = $str  str1 = $str1")
    }

    private fun getStatus() {
        var m = 1024*1024
        var r = Runtime.getRuntime();
        var maxMemory = r.maxMemory()
        var totalMemory = r.totalMemory()
        var free = r.freeMemory()
        var used = r.totalMemory() - r.freeMemory()
        tv.append(ArchitectureUtil.getArchType(this) + "位系统\n")

        tv.append("pid = ${Process.myPid()}\n")
        tv.append("最大可用内存 = $maxMemory B ${Runtime.getRuntime().maxMemory()/m}M\n")
        tv.append("当前可用内存 = $totalMemory B ${totalMemory/m}M\n")
        tv.append("当前空闲内存 = $free B ${Runtime.getRuntime().freeMemory()/m}M\n")
        tv.append("当前已用内存 = $used B ${(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/m}M")
        //Os.readlink()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun proc(view: View) {
        var appVmSize = InfoGenerater.getAppVmSize();
        LogUtil.log("appVmSize = $appVmSize")
        var appVmRss = InfoGenerater.getAppVmRss();
        LogUtil.log("appVmRss = $appVmRss")
        Thread(){
            //InfoGenerater.copySmaps(this)
            //InfoGenerater.getProcessFd()
            var fdMaxSize = InfoGenerater.getAppMaxFDSize()
            LogUtil.log("fdMaxSize = $fdMaxSize")
            var fdSize = InfoGenerater.getAppFDSize()
            LogUtil.log("fdSize = $fdSize")
            var fdSizes = InfoGenerater.getAppFDSizes()
            LogUtil.log("fdSizes = $fdSizes")
            var maxOpenFiles = InfoGenerater.getAppMaxOpenFiles()
            LogUtil.log("maxOpenFiles = $maxOpenFiles")
            var threadsSize = InfoGenerater.getAppThreadsSize();
            LogUtil.log("threadsSize = $threadsSize")
            var threads = InfoGenerater.getAppThreads();
            LogUtil.log("threads.size = ${threads.size} ---${threads.toString().length}--- threads = $threads")

            var nativePss = InfoGenerater.getNativeMemSize()
            LogUtil.log("nativePss = $nativePss")
        }.start()

    }
}