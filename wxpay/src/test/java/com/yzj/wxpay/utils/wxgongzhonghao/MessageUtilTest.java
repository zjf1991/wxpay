package com.yzj.wxpay.utils.wxgongzhonghao;

import org.junit.Test;

import static org.junit.Assert.*;

public class MessageUtilTest {

    @Test
    public void setTempelete() {

       String result= MessageUtil.setTempelete();
        System.out.println(result);
    }

    @Test
    public void getTempelete() {

        String result= MessageUtil.getTempelete();
        System.out.println(result);
    }
    @Test
    public void sendTempelete() {

        String result= MessageUtil.sendTempeleteMessage();
        System.out.println(result);
    }

    @Test
    public void createMenu() {

        String result= MessageUtil.getToken();
        System.out.println(result);
    }

    @Test
    public void upload() {
        String path="C:\\Users\\Administrator\\Desktop\\yzj\\源自家分享活动\\images\\abouthome.png";
        Object result= null;
        try {
            result = MessageUtil.UploadMeida("image",path);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }
}