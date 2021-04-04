package com.jenkin.proxy.server.utils;

import java.io.*;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/4 10:09
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class TestBufferdReader {
    public static void main(String[] args) throws IOException {
        File file = new File("D:\\tempDir\\test\\1.txt");
//        File file = new File("D:\\tempDir\\test\\测试.jpeg");
        File dest = new File("D:\\tempDir\\test\\dest.jpeg");
        if (!dest.exists()) {
            dest.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(dest);
        InputStream inputStream = new FileInputStream(file);
        InputStreamReader fileReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String s = null;
        StringBuilder sb = new StringBuilder("1");

        while(true){
            byte read = (byte) inputStream.read();
            System.out.println(read);
            if(read==-1) break;
        }


        fileOutputStream.write(sb.toString().getBytes());
        inputStream.close();
        fileOutputStream.close();
        fileReader.close();
        bufferedReader.close();





    }
}
