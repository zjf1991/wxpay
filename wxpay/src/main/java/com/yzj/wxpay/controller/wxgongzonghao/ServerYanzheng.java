package com.yzj.wxpay.controller.wxgongzonghao;


import com.yzj.wxpay.utils.wxgongzhonghao.MessageUtil;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static com.yzj.wxpay.utils.wxgongzhonghao.MessageUtil.parseXML;
import static com.yzj.wxpay.utils.wxgongzhonghao.ServerYanzhengUtil.checkSignature;

/**
 * 公众号开发类
 */
@RestController
public class ServerYanzheng {


    /**
     * 验证服务器配置
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/app/yzj/wx/yanzheng")
    public Object yanzheng(HttpServletRequest request, HttpServletResponse response){

        try {
            String   signature=request.getParameter("signature");
            String   timestamp=request.getParameter("timestamp");
            String   nonce=request.getParameter("nonce");
            String   echostr=request.getParameter("echostr");

            PrintWriter printWriter=response.getWriter();
           boolean result= checkSignature(signature,timestamp,nonce);
           if(result){
               printWriter.write(echostr);
           }
           printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 回复消息
     * @param request
     * @param response
     */
    @PostMapping ("/app/yzj/wx/yanzheng")
    public void remessage(HttpServletRequest request, HttpServletResponse response){
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");


            Map<String,Object> data= parseXML(request);
            System.out.println(data);
            String msgTepy=(String) data.get("MsgType");
            if(msgTepy.equals("text")){  //文本消息
                if(data.get("Content").equals("音乐")){
                   /* MessageUtil.reMusic()*/
                }else if(data.get("Content").toString().contains("源自家")){
                    MessageUtil.reTuWen(response,data);
                }else {
                    String content="不想搭理你！！！！！！！";
                    MessageUtil.reText(response,data,content);
                }
            }else if(msgTepy.equals("image")){  //图片消息
                MessageUtil.reImage(response,data);
            }else if(msgTepy.equals("voice")){  //语音消息
                if(data.get("Recognition").toString().contains("源自家")){
                    MessageUtil.reTuWen(response,data);
                }else {
                    String content="说点别的吧！";
                    MessageUtil.reText(response,data,content);
                }
            }else if(msgTepy.equals("video")){  //视频消息

            }else if(msgTepy.equals("shortvideo")){  //小视频消息

            }else if(msgTepy.equals("location")){  //地理位置消息

            }else if(msgTepy.equals("link")){  //链接消息

            }else if(msgTepy.equals("event")){  //关注公众号
                if(data.get("Event").toString().equals("subscribe")){
                    String content="欢迎关注我的公众号！";
                    MessageUtil.reText(response,data,content);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 微信网页授权登录
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/app/yzj/wx/authorize/getcode")
    public Object shouquan(HttpServletRequest request, HttpServletResponse response){
        try {
            //获取code
            String code =request.getParameter("code");
            String appid="wx0b314a9ba53bcfc4";
            String secret="26f07a05ea237ac8a180342e6bf4bbf7";
            //用code换取token
            String result=MessageUtil.getAccess_token(appid,secret,code);
            JSONObject object=new JSONObject(result);
            //拉取用户信息
            String Userinfo=MessageUtil.getUserinfo(object.get("access_token").toString(),object.get("openid").toString());
            JSONObject user=new JSONObject(Userinfo);
            String headimgurl=user.get("headimgurl").toString();
            System.out.println(headimgurl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
