package com.yzj.wxpay.controller.wxgongzonghao;

import com.alibaba.fastjson.JSONObject;
import com.yzj.wxpay.utils.RetJson;
import com.yzj.wxpay.utils.wxpay.HttpClientUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
public class AccessToken {


       private  static   String token=null;
       private  static   long Expire_time=0;


    /**
     * 获取tickets和签名
     */
    @RequestMapping(value = "/app/api/v1/getAccessToken", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
    public RetJson handleMenuAdd(HttpServletRequest request) {
        RetJson retJson = new RetJson();
        Map<String,Object> data=new HashMap<>();

        try {
            token=getToken();
            System.out.println("token:"+token);
            String tickets=HttpClientUtil.doGet("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+token+"&type=jsapi");
            JSONObject jsonObjectticket= JSONObject.parseObject(tickets);
            String ticket=(String) jsonObjectticket.get("ticket");
            data.put("ticket",ticket);
            String url=URLDecoder.decode(request.getParameter("url"),"UTF-8");
            Map<String, String> sign=AccessToken.sign(ticket,url);
            data.put("sign",sign);
            retJson.ret = 0;
            retJson.msg = "获取成功！";
            retJson.data=data;
        } catch (Exception e) {
            e.printStackTrace();
            retJson.ret = 1;
            retJson.msg = "获取失败！";
        }
        return retJson;
    }


    /**
     * 获取AccessToken
     */
    public String getToken() {
        try {
            if(token==null||Expire_time<new Date().getTime()){
                String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
                url= url.replaceAll("APPID","wx5a5442e966841816");
                url= url.replaceAll("APPSECRET","d2550a1b854f1b9994f722126d9890af");
                String result=HttpClientUtil.doGet(url);
                JSONObject jsonObject= JSONObject.parseObject(result);
                token=(String) jsonObject.get("access_token");
                long   etime=Long.valueOf(jsonObject.get("expires_in").toString()) ;
                Expire_time=new Date().getTime()+etime*1000;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 签名
     * @param jsapi_ticket
     * @param url
     * @return
     */
    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        // 注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str
                + "&timestamp=" + timestamp + "&url=" + url;

        System.out.println("[string1] = " + string1);

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());

            System.out.println("[signature] = " + signature);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appId", "wxdf21e75820dc5bd0");

        System.out.println("[ret] = " + ret);

        return ret;
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

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }



    @RequestMapping(value = "/app/api/v1/getException", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
    public RetJson getResult(){
        RetJson retJson =new RetJson();
        retJson.ret=0;
        retJson.msg="请求成功";
        return retJson ;
    }

}
