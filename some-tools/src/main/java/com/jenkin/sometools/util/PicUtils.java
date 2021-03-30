package com.jenkin.sometools.util;

import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

/**
 * @author jenkin
 * @className PicUtils
 * @description TODO
 * @date 2021/3/10 10:18
 */
public class PicUtils {

    //设置APPID/AK/SK
    public static final String APP_ID = "23771630";
    public static final String API_KEY = "L3BgvhfzYffvrp3atr8IFbAj";
    public static final String SECRET_KEY = "0hoih6m0KQ9GNmSTTH3KXzFtd6exCQ5L";

    public static void main(String[] args) throws IOException {
        // 初始化一个AipBodyAnalysis
        AipBodyAnalysis client = new AipBodyAnalysis(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("type", "foreground");
        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
//        char a = '\0';
        // 调用接口

        String path = "C:\\Users\\zhouj\\Pictures\\Saved Pictures\\微信图片_20210310093714.jpg";
        JSONObject res = client.bodySeg(path,options);
        String s =res.getString("foreground");
        byte[] decode = Base64.getDecoder().decode(s);
        File file = new File("C:\\Users\\zhouj\\Pictures\\Saved Pictures\\after.png");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(decode);
        System.out.println(s);

    }
}
