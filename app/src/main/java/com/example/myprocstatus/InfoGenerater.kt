package com.example.myprocstatus

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Debug
import android.system.Os
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.File
import java.io.RandomAccessFile
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * 信息生成类
 */
class InfoGenerater {
    companion object{
        @JvmStatic
        fun getAppVmSize():String{
            var line = "-0"
            RandomAccessFile("/proc/${android.os.Process.myPid().toString()}/status", "r").use {

                while (true){
                    line = it.readLine() ?: break

                    if (line.contains("VmSize")) { //VmSize:	 5889940 kB
                        val arr: Array<String> = line.split(":".toRegex()).toTypedArray() //[VmSize, 	 2619520 kB]
                        val arr2 = arr[1].trim { it <= ' ' }.split(" ".toRegex()).toTypedArray()//arr2 = [2619520, kB]
                        val value = arr2[0]//value = 2619520

                        var longValue = value.toLong();
                        var result = formatStringByK(longValue)
                        return result
                    }
                }
            }

            return line;
        }
        @JvmStatic
        fun getAppVmRss():String{
            var line = "-0"
            RandomAccessFile("/proc/${android.os.Process.myPid().toString()}/status", "r").use {

                while (true){
                    line = it.readLine() ?: break

                    if (line.contains("VmRSS")) { //VmSize:	 5889940 kB
                        val arr: Array<String> = line.split(":".toRegex()).toTypedArray() //[VmRSS, 	 89840 kB]
                        val arr2 = arr[1].trim { it <= ' ' }.split(" ".toRegex()).toTypedArray()//arr2 = [89840, kB]
                        val value = arr2[0]//value = 89840

                        var longValue = value.toLong();
                        var result = formatStringByK(longValue)
                        return result
                    }
                }
            }

            return line;
        }

        /**
         * /proc/PID/smaps
         */
        fun copySmaps(context: Context){
            copyProcFile(context, "smaps")
        }
        @JvmStatic
        fun copyProcFile(context: Context, name: String):String{
            var line:String = "-0"
            RandomAccessFile("/proc/${android.os.Process.myPid().toString()}/$name", "r").use {
                var path = context.externalCacheDir?.absolutePath + File.separator + name;
                LogUtil.log(context,"getAppSmaps path = $path")
                /** 以读写的方式建立一个RandomAccessFilee对象 **/
                var raf = RandomAccessFile(path, "rw");


                while (true){
                    line = it.readLine() ?: break

                    // 将记录指针移动到文件最后
                    raf.seek(raf.length());
                    raf.writeChars(line + "\n")

                }
                LogUtil.log(context,"getAppSmaps = $line")
            }

            return line;
        }

        /**
         * 当前系统内存空间当前可用量，单位是byte，
         */
        @JvmStatic
        fun getSystemAvailMem(context: Context):String{
            var manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

            var info = ActivityManager.MemoryInfo()

            manager.getMemoryInfo(info)
            var memory = info.availMem // 单位是byte，内存空间当前可用量
            //var totalMemory = info.totalMem //内存的总量
            return formatStringByBytes(memory)
        }
        /**
         * 当前系统内存空间总量，单位是byte，
         */
        @JvmStatic
        fun getSystemTotalMem(context: Context):String{
            var manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

            var info = ActivityManager.MemoryInfo()

            manager.getMemoryInfo(info)
            //var memory = info.availMem // 单位是byte，内存空间当前可用量
            var totalMemory = info.totalMem //内存的总量
            return formatStringByBytes(totalMemory)
        }
        /**
         * 当前系统低内存阀值，单位是byte，
         */
        @JvmStatic
        fun getSystemMemThreshold(context: Context):String{
            var manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

            var info = ActivityManager.MemoryInfo()

            manager.getMemoryInfo(info)
            //var memory = info.availMem // 单位是byte，内存空间当前可用量
            //var totalMemory = info.totalMem //内存的总量
            var threshold = info.threshold
            return formatStringByBytes(threshold)
        }
        /**
         * 当前app运行时最大内存，单位是byte，
         */
        @JvmStatic
        fun getAppRuntimeMaxMem():String{
            var max = Runtime.getRuntime().maxMemory()
            Runtime.getRuntime().totalMemory()
            return formatStringByBytes(max)
        }
        /**
         * 当前app运行时当前可用总内存，单位是byte，
         */
        @JvmStatic
        fun getAppRuntimeTotalMem():String{
            var total = Runtime.getRuntime().totalMemory()
            return formatStringByBytes(total)
        }
        /**
         * 当前app运行时当前可用内存，单位是byte，
         */
        @JvmStatic
        fun getAppRuntimeFreeMem():String{
            var free = Runtime.getRuntime().freeMemory()
            return formatStringByBytes(free)
        }
        /**
         * 当前app运行时当前已用内存，单位是byte，
         */
        @JvmStatic
        fun getAppRuntimeUsedMem():String{
            var runtime = Runtime.getRuntime()
            var total = runtime.totalMemory()
            var free = runtime.freeMemory()
            return formatStringByBytes(total - free)
        }


        @JvmStatic
        fun getAppMaxFDSize():String{
            var line = "-0"
            RandomAccessFile("/proc/${android.os.Process.myPid().toString()}/status", "r").use {

                while (true){
                    line = it.readLine() ?: break

                    if (line.contains("FDSize")) { //FDSize: 128
                        val arr: Array<String> = line.split(":".toRegex()).toTypedArray() //[FDSize, 	 128]
                        val value = arr[1].trim()

                        return value
                    }
                }
            }

            return line;
        }

        @JvmStatic
        fun getAppFDSize(): String {
            var result = "-1"
            try {
                //RandomAccessFile reader2 = new RandomAccessFile("/proc/"+ pid +"/fd", "r");///process/pid/smaps
                val filePath = "/proc/${android.os.Process.myPid()}/fd"
                val fd = File(filePath)
                Log.d("ddebug", "fd.list().length = " + fd.list().size)
                //reader2.close();
                result = fd.list().size.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }finally {
                return result
            }
        }
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        @JvmStatic
        fun getAppFDSizes() :List<String>{
            var result:ArrayList<String>  = arrayListOf()
            try {

                val filePath = "/proc/${android.os.Process.myPid()}/fd"
                val fd = File(filePath)
                Log.d("ddebug", "fd.list().length = " + fd.list().size)

                for (path in fd.list()) {
                    //Log.d("ddebug", "path = $path")
                    val str = filePath + File.separator + path
                    //Log.d("ddebug", "str = $str")
                    try {
                        val readlink = Os.readlink(str)
                        //Log.d("ddebug", "readlink = $readlink")
                        result.add(readlink)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }finally {
                return result
            }
        }
        @JvmStatic
        fun getAppMaxOpenFiles(): String {
            var result = "-1"
            RandomAccessFile("/proc/${android.os.Process.myPid().toString()}/limits", "r").use {
                var line = ""
                while (true){
                    line = it.readLine() ?: break
                    var regex = "Max open files"
                    if (line.contains(regex)) { //Max open files            32768                32768                files
                        line = line.replace(regex,"",true).trim()//32768                32768                files
                        val arr: Array<String> = line.split(" ".toRegex()).toTypedArray() //[32768, , , , , , , , , , , , , , , , 32768, , , , , , , , , , , , , , , , files]
                        //LogUtil.log("arr = " + arr.contentToString())
                        //LogUtil.log("arr[0] = " + arr[0])
                        result = arr[0]
                    }
                }
            }
            return result
        }
        @JvmStatic
        fun getAppThreadsSize():String{
            var line = "-0"
            RandomAccessFile("/proc/${android.os.Process.myPid().toString()}/status", "r").use {

                while (true){
                    line = it.readLine() ?: break

                    if (line.contains("Threads")) { //Threads:        41
                        val arr: Array<String> = line.split(":".toRegex()).toTypedArray() //[FDSize, 	 128]
                        val value = arr[1].trim()

                        return value
                    }
                }
            }
            return line;
        }

        @JvmStatic
        fun getAppThreads():ArrayList<String>{
            var list:ArrayList<String> = arrayListOf()

            var dirPath = "/proc/${android.os.Process.myPid()}/task"
            var dir = File(dirPath)
            var subDirs = dir.list()
            for (threadDir in subDirs){
                var path = "$dirPath/$threadDir/status" // /proc/22253/task/22278/status

                var line = "-0"
                RandomAccessFile(path, "r").use {

                    while (true){
                        line = it.readLine() ?: break

                        if (line.contains("Name")) { //Name:   Jit thread pool
                            val arr: Array<String> = line.split(":".toRegex()).toTypedArray() //[Name,   Jit thread pool]
                            val value = arr[1].trim()
                            list.add(value)
                        }
                    }
                }
            }

            return list;
        }

        /**
         * 这个方法要注意，此方法运行时不能用profiler分析性能和内存，不然会引起崩溃
         */
        fun getNativeMemSize():String{
            var info = Debug.MemoryInfo()
            Debug.getMemoryInfo(info)
            var nativePss = info.nativePss//以K为单位
            nativePss.toLong()
            var longValue = nativePss.toLong()
            //LogUtil.log("nativePss.toLong() === " + longValue)
            var value = formatStringByK(longValue)
            return value
        }
        /**
         * 原数据单位为KB的格式化
         */
        fun formatStringByK(value: Long):String{

            val M = 1024
            val G = 1024 * 1024
            var result = "-0"

            // LogUtil.log("formatStringByK value=$value")

            //DecimalFormat format = new DecimalFormat("#.00");
            val format = NumberFormat.getNumberInstance()
            // 保留两位小数
            format.maximumFractionDigits = 2
            when{
                value > G ->{
                    var number = value.toDouble()/G;
                    result = "${format.format(number)} G"
                }
                value > M ->{
                    var number = value.toDouble()/M;
                    result = "${format.format(number)} M"
                }
                else -> {
                    result = "$value K"
                }
            }
            return result
        }
        /**
         * 原数据单位为Bytes的格式化
         */
        fun formatStringByBytes(value: Long):String{
            Runtime.getRuntime().freeMemory()
            val K = 1024
            val M = K * 1024
            val G = M * 1024
            var result = "-0"

            //LogUtil.log("formatStringByBytes value=$value")

            //DecimalFormat format = new DecimalFormat("#.00");
            val format = NumberFormat.getNumberInstance()
            // 保留两位小数
            format.maximumFractionDigits = 2
            when{
                value > G ->{
                    var number = value.toDouble()/G;
                    result = "${format.format(number)} G"
                }
                value > M ->{
                    var number = value.toDouble()/M;
                    result = "${format.format(number)} M"
                }
                value > K ->{
                    var number = value.toDouble()/K;
                    result = "${format.format(number)} K"
                }
                else -> {
                    result = "$value B"
                }
            }
            return result
        }
    }
}