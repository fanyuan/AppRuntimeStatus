package com.example.myprocstatus;

import android.os.Build;
import android.os.Debug;
import android.system.Os;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
    /**
     * 读文件
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readFileSdcardFile(String fileName){
        String res="";
        try{
            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();

            byte [] buffer = new byte[length];
            fin.read(buffer);

            //res = EncodingUtils.getString(buffer, "UTF-8");
            res = new String(buffer,"UTF-8");
            Log.d("ddebug","length = "+ length +"res = " + res);

            fin.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.d("ddebug","123 res = " + res);
        return res;
    }

    /**
     * 读文件
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readSDFile(String fileName) throws IOException {

        File file = new File(fileName);

        FileInputStream fis = new FileInputStream(file);

        int length = fis.available();
        Log.d("ddebug","abc   length = "+ length );
        byte [] buffer = new byte[length];
        fis.read(buffer);

        //res = EncodingUtils.getString(buffer, "UTF-8");
        String res = new String(buffer, "UTF-8");
        fis.close();
        return res;
    }
    public static void getProcessStatus(String pid) {
        try {
            RandomAccessFile reader2 = new RandomAccessFile("/proc/"+ pid +"/status", "r");
            String str;
            while ((str = reader2.readLine()) != null){
                if(str.contains("VmSize")){//VmSize:	 5889940 kB
                    String[] arr = str.split(":");
                    Log.d("ddebug","arr = " + Arrays.toString(arr));
                    String[] arr2 = arr[1].trim().split(" ");
                    Log.d("ddebug","arr2 = " + Arrays.toString(arr2));
                    String value = arr2[0];
                    Log.d("ddebug","value = " + value);
                    long m = Long.parseLong(value) / 1024;
                    Log.d("ddebug","value/m = " + m + "M");
                    //return;
                }
                Log.d("ddebug",str);
            }
            reader2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void getProcessSmaps(String pid) {
        Log.d("ddebug","---------------getProcessSmaps---------------");
        try {
            RandomAccessFile reader2 = new RandomAccessFile("/proc/"+ pid +"/smaps", "r");///process/pid/smaps
            String str;
            while ((str = reader2.readLine()) != null){
                Log.d("ddebug",str);
            }
            reader2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getProcessLimits(String pid) {
        Log.d("ddebug","---------------getProcessLimits---------------");
        try {
            RandomAccessFile reader2 = new RandomAccessFile("/proc/"+ pid +"/limits", "r");///process/pid/smaps
            String str;
            while ((str = reader2.readLine()) != null){
                Log.d("ddebug",str);
            }
            reader2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void getProcessFd(String pid) {
        Log.d("ddebug","---------------getProcessFd---------------");
        try {
            //RandomAccessFile reader2 = new RandomAccessFile("/proc/"+ pid +"/fd", "r");///process/pid/smaps
            String filePath = "/proc/" + pid + "/fd";
            File fd = new File(filePath);
            Log.d("ddebug","fd.list().length = " + fd.list().length);
            //reader2.close();
            for (String path:fd.list()){
                Log.d("ddebug","path = " + path);
                String str = filePath + File.separator + path;
                Log.d("ddebug","str = " + str);
                try{
                    String readlink = Os.readlink(str);
                    Log.d("ddebug","readlink = " + readlink);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static double getProcessCpuUsage(String pid) {
        try {

            RandomAccessFile reader2 = new RandomAccessFile("/proc/"+ pid +"/status", "r");
            String load2 = reader2.readLine();
            Log.d("ddebug","load2 = " + load2);
            String[] toks2 = load2.split(" ");


            load2 = reader2.readLine();
            Log.d("ddebug","load3 = " + load2);

            while ((load2 = reader2.readLine()) != null){
                Log.d("ddebug","while load2 = " + load2);
            }

            try {
                Thread.sleep(360);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return 0.0;
    }
    public static String getStr(String path) {
        String result = "";
        Log.d("ddebug","---------------getStr---------------" + path);
        try {
            RandomAccessFile reader2 = new RandomAccessFile(path, "r");///process/pid/smaps
            String str;
            while ((str = reader2.readLine()) != null){
                if(str.contains("Name:")){
                    //Name:   hwuiTask5
                    result = str.split(":")[1].trim();
                    Log.d("ddebug","0thread name =" + result + "---");
                }
            }
            reader2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return result;
        }
    }
    public static List<String> listThreads(String pid) {
        String path = "/proc/"+ pid +"/task/";
        List<String> names = new ArrayList<>();
        File dir = new File(path);
        String[] list = dir.list();
        Log.d("debug","list = " + Arrays.toString(list));
        for(String threadId : list){
            ///proc/26721/task/26733/status
            String pathThread = path + threadId + File.separator + "status";//path = path + list[0] + File.separator + "status";
            names.add(getStr(pathThread));
        }
        LogUtil.log("threads = " + names.toString());
        return names;
    }
}
