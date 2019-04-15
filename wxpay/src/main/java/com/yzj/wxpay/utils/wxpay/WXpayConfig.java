package com.yzj.wxpay.utils.wxpay;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

public class WXpayConfig {
    //appid
    public  static  final  String APPID="wx68491b7083e86211";
    //证书
    public  static  final  String MchID="1493793662";
    //key
    public  static  final  String Key="YZJ20171200611335524424488888888";
    //统一下单接口
    public  static  final  String UNIFIEDORDER_URL_SUFFIX="https://api.mch.weixin.qq.com/pay/unifiedorder";
    //退款接口
    public  static  final  String REFUND_URL="https://api.mch.weixin.qq.com/secapi/pay/refund";
    //证书地址
    public  static  final  String apiclient_cert_FORMAL="/mnt/yzjtest/apiclient_cert.p12";
    public  static  final  String apiclient_cert_TEST="F:\\mnt\\apiclient_cert.p12";
    //回调接口
    public  static final String FORMALSERVERADDRESS="http://test.yuanzijia.com:8066";



    /**
     * 证书使用
     * 微信退款
     */
    public static String wxPayBack(String url, String data) throws Exception {
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(apiclient_cert_TEST));
        WXPayConfigImpl config = WXPayConfigImpl.getInstance();
        String result="";
        try {
            keyStore.load(instream, config.getMchID().toCharArray());
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, config.getMchID().toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            HttpPost httppost = new HttpPost(url);
            StringEntity entitys = new StringEntity(data);
            httppost.setEntity((HttpEntity) entitys);
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String text="";
                    String t="";
                    while ((text=bufferedReader.readLine()) != null) {
                        t+=text;
                    }
                    byte[] temp=t.getBytes("utf-8");//这里写原编码方式
                    String newStr=new String(temp,"utf-8");//这里写转换后的编码方式
                    result=newStr;
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return result;
    };

}
