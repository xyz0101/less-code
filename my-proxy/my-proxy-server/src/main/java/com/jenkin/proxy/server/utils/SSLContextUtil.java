package com.jenkin.proxy.server.utils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class SSLContextUtil {
    public static String keyStoreName = "C:\\Program Files\\Java\\jdk1.8.0_211\\jre\\lib\\security\\cacerts";
    public static char[] keyStorePwd = "changeit".toCharArray();
    public static char[] keyPwd = "123456".toCharArray();
    public static SSLContext getSSLContext(){
        SSLContext context = null;
        try{
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            //装载生成的seckey
            try(InputStream in = new FileInputStream(keyStoreName)){
                keyStore.load(in, keyStorePwd);
            }

            //初始化KeyManagerFactory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keyPwd);

            //初始化SSLContext
            context = SSLContext.getInstance("TLSv1.2");
            context.init(kmf.getKeyManagers(), null, null);
        } catch (IOException | UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException | KeyManagementException | CertificateException e) {
            e.printStackTrace();
        }

        return context;
    }
}
