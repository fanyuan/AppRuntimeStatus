package com.example.myprocstatus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;

public class StackTraceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack_trace);
    }
    public void start(View view) {
        //methodA();
        FileUtil.listThreads(Process.myPid()+"");
    }
    private void methodA(){
        System.out.println("------进入methodA----------");
        methodB();
    }

    private void methodB(){
        System.out.println("------进入methodB----------");
        methodC();
    }

    /**
     * 遍历StackTrace中的内容并遍历StackTraceElement数组
     * 请注意观察此处的输出信息.
     */
    private void methodC(){
        System.out.println("------进入methodC----------");
        StackTraceElement stackTraceElements[]=Thread.currentThread().getStackTrace();
        for (int i = 0; i < stackTraceElements.length; i++) {
            String threadName=Thread.currentThread().getName();
            long threadID=Thread.currentThread().getId();
            StackTraceElement stackTraceElement=stackTraceElements[i];
            String className=stackTraceElement.getClassName();
            String methodName=stackTraceElement.getMethodName();
            String fileName=stackTraceElement.getFileName();
            int lineNumber=stackTraceElement.getLineNumber();
            System.out.println("StackTraceElement数组下标 i="+i+",threadID="+threadID+",threadName="+threadName+",fileName="
                    +fileName+",className="+className+",methodName="+methodName+",lineNumber="+lineNumber);
            System.out.println("-------------");
        }
        methodD();
    }

    /**
     * 在methodC中遍历StackTraceElement数组.
     * 但是发现下标为2和3的元素包含的信息是最有用的.
     * 于是在这里单独获取.
     */
    private void methodD(){
        System.out.println("------进入methodD----------");
        StackTraceElement stackTraceElement=null;
        String threadName=Thread.currentThread().getName();
        long threadID=Thread.currentThread().getId();
        stackTraceElement=Thread.currentThread().getStackTrace()[2];
        String className=stackTraceElement.getClassName();
        String methodName=stackTraceElement.getMethodName();
        String fileName=stackTraceElement.getFileName();
        int lineNumber=stackTraceElement.getLineNumber();
        System.out.println("该方法的信息:threadID="+threadID+",threadName="+threadName+",fileName="+fileName+
                ",className="+className+",methodName="+methodName+",lineNumber="+lineNumber);
        stackTraceElement=Thread.currentThread().getStackTrace()[3];
        className=stackTraceElement.getClassName();
        methodName=stackTraceElement.getMethodName();
        fileName=stackTraceElement.getFileName();
        lineNumber=stackTraceElement.getLineNumber();
        System.out.println("该方法的调用者的信息:threadID="+threadID+",threadName="+threadName+",fileName="+fileName+
                ",className="+className+",methodName="+methodName+",lineNumber="+lineNumber);
    }


}