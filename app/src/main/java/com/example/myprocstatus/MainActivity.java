package com.example.myprocstatus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug","MainActivity = onCreate000" );
        super.onCreate(savedInstanceState);
        Log.d("debug","MainActivity = onCreate111" );
        setContentView(R.layout.activity_main);
        initPermission();
        Log.d("debug","MainActivity = onCreate" );

    }

    @Override
    protected void onResume() {
        Log.d("debug","MainActivity = onResume000" );
        super.onResume();
        Log.d("debug","MainActivity = onResume111" );
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runingappinfos = am.getRunningAppProcesses();
        //Log.d("debug","RunningAppProcesses = " + runingappinfos.size());
        Log.d("debug","MainActivity = onResume" );
    }

    public void memory(View view) {
        //toast("process");
        startActivity(new Intent(this, MemoryActivity.class));
    }
    public void proc(View view) {
        //toast("process");
        startActivity(new Intent(this,ProcActivity.class));
    }
    public void stackTrace(View view) {
        //toast("process");
        startActivity(new Intent(this,StackTraceActivity.class));
    }

    void initPermission(){
        boolean flag2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED;
        if (flag2) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},123);
        }
    }
    private void toast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}