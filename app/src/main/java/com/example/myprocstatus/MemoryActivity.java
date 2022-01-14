package com.example.myprocstatus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MemoryActivity extends AppCompatActivity {

    private long K = 1024;
    private long M = 1024 * 1024;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        tv = findViewById(R.id.tv);
        getMemory();
    }


    public void test(View v){
        getMemory();

        String systemAvailMem =InfoGenerater.getSystemAvailMem(this);
        String systemTotalMem =InfoGenerater.getSystemTotalMem(this);
        String systemMemThreshold =InfoGenerater.getSystemMemThreshold(this);

        LogUtil.log("system内存空间当前可用:" + systemAvailMem);
        LogUtil.log("system内存的总量:" + systemTotalMem);
        LogUtil.log("system系统低内存阀值:" + systemMemThreshold);
        LogUtil.log("----------------------------------");

        String appRuntimeMax = InfoGenerater.getAppRuntimeMaxMem();
        String appRuntimeTotal = InfoGenerater.getAppRuntimeTotalMem();
        String appRuntimeFree = InfoGenerater.getAppRuntimeFreeMem();
        String appRuntimeUsed = InfoGenerater.getAppRuntimeUsedMem();

        LogUtil.log("appRuntime最大可用内存:" + appRuntimeMax);
        LogUtil.log("appRuntime当前可用内存:" + appRuntimeTotal);
        LogUtil.log("appRuntime当前空闲内存:" + appRuntimeFree);
        LogUtil.log("appRuntime当前已用内存:" + appRuntimeUsed);
    }
    private void getMemory(){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();

        manager.getMemoryInfo(info);
        long memory = info.availMem;// 单位是byte，内存空间当前可用量

        long totalMemory = info.totalMem;//内存的总量

        tv.append("System:" + "\n");
        tv.append("内存空间当前可用:" + (memory/M) + "M\n");
        tv.append("内存的总量:" + (totalMemory/M) + "M\n");
        tv.append("当系统剩余内存低于"+(info.threshold/M)+"M时就看成低内存运行");

        tv.append("\nApp:" + "\n");
        long m = 1024*1024;
        Runtime r = Runtime.getRuntime();
        double max = r.maxMemory();
        double total = r.totalMemory();
        double free = r.freeMemory();
        double used = total - free;

        //DecimalFormat format = new DecimalFormat("#.00");
        NumberFormat format = NumberFormat.getNumberInstance();
        // 保留两位小数
        format.setMaximumFractionDigits(2);
        DecimalFormat format2 = new DecimalFormat("#");
        tv.append("最大可用内存 = " + format2.format(max) + " B " +format.format(max/m)+ "M\n");
        tv.append("当前可用内存 = "+ format2.format(total) +" B " +format.format(total/m)+ "M\n");
        tv.append("当前空闲内存 = "+ format2.format(free) +" B " +format.format(free/m)+ "M\n");
        tv.append("当前已用内存 = "+ format2.format(used) +" B " +format.format(used/m) + "M");
    }
}