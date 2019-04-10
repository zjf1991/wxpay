package com.yzj.wxpay.utils.wxgongzhonghao;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * 服务器配置验证工具类
 */

public class ServerYanzhengUtil {

    private  static  final String token="3XhtaBgRTGYz4PgL";

    /**
     * 验证方法
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static  boolean checkSignature(String signature,String  timestamp,String nonce){


        //将token,timestamp,nonce进行字典排序
        String[] strings={token,timestamp,nonce};
        Arrays.sort(strings);

        ////将token,timestamp,nonce拼成字符串
        StringBuilder content=new StringBuilder();
        for(int i=0;i<strings.length;i++){
            content.append(strings[i]);
        }

        //进行sha1加密
       String sign= sign(content.toString());
        if(signature.equals(sign)){
           return true;
        }
        return false;
    }

    /**
     * 签名
     * @return
     */
    public static String sign(String str) {
        String signature = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(str.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());

            System.out.println("[signature] = " + signature);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return signature;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

}
