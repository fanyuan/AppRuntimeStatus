package com.example.myprocstatus;

public class ByteUtil {
    /**
     * 容量字节转兆
     * @param bytes
     * @return
     */
    public static Long b2m(Long bytes){
        int m = 1024 * 1024;
        return  bytes/m;
    }
}
