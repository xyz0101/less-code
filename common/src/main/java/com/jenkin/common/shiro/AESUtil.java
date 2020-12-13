package com.jenkin.common.shiro;



import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;





@Slf4j
public class AESUtil {

    public static final String DEFAULT_AES_KEY = "02200059#nobody";

    /**
     * @Description: AES加密
     * @param content 需要加密的内容
     * @return
     */
    public static String encrypt(String content){
        return encrypt(content,DEFAULT_AES_KEY);
    }
    /**
     * @Description: AES加密
     * @param content 需要加密的内容
     * @return
     */
    public static String decrypt(String content){
        return decrypt(content,DEFAULT_AES_KEY);
    }
    /**
     * @Description: AES加密
     * @param content 需要加密的内容
     * @return
     */
    public static String encrypt(String content, String AESKey){
        try{
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(AESKey.getBytes());
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化

            byte[] encryptResult = cipher.doFinal(byteContent);    //加密后接口

            return Base64.encode(encryptResult); // 加密
        }catch (Exception e){
            log.error("AES加密时出现异常：【content："+content+";AESPwd："+AESKey+"】",e);
        }
        return null;
    }

    /**
     *
     * @Description: AES解密
     * @param content  待解密内容
     * @return
     */
    public static String decrypt(String content, String AESKey){
        try{
            byte[] contentByte = Base64.decode(content);// 先用base64解密
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(AESKey.getBytes());

            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(contentByte);
            return new String(result,"utf-8"); // 解密
        }catch(Exception e){
            log.error("AES解密时出现异常：[content："+content+";AESPwd："+AESKey+"]");
            throw new RuntimeException(e);
        }
    }


}