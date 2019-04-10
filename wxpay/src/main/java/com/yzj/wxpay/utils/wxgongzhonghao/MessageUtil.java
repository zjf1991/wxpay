package com.yzj.wxpay.utils.wxgongzhonghao;


import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.yzj.wxpay.entity.*;
import com.yzj.wxpay.utils.wxpay.HttpClientUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * 处理回复消息的类
 */
public class MessageUtil {


    private  static   String token=null;
    private  static   long Expire_time=0;
    private  static  String APPID="wx5a5442e966841816";
    private  static  String APPSECRET="d2550a1b854f1b9994f722126d9890af";


    /**
     * 接受xml消息转换成map
     * @param request
     * @return
     */
    public static Map<String,Object> parseXML(HttpServletRequest request){

        Map<String,Object> data=new HashMap<>();
        SAXReader saxReader=new SAXReader();
        try {
            Document doc=saxReader.read(request.getInputStream());

            //获取根节点
            Element root= doc.getRootElement();
            //获取根节点的所有子节点
            List<Element> elementList=root.elements();
            for (int i=0;i<elementList.size();i++){
                //获取节点名
                String name=elementList.get(i).getName();
                String txt=elementList.get(i).getTextTrim();
                data.put(name,txt);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 回复文本消息
     * @param data
     * @return
     */
    public static String reText(HttpServletResponse response, Map<String,Object> data,String content){
        try {
            xStream.alias("xml",TextMessageEntity.class);
            TextMessageEntity textMessageEntity=new TextMessageEntity();
            textMessageEntity.setToUserName(data.get("FromUserName").toString());
            textMessageEntity.setCreateTime(new Date().getTime());
            textMessageEntity.setFromUserName(data.get("ToUserName").toString());
            textMessageEntity.setMsgType("text");
            textMessageEntity.setContent(content);

            String xml=  xStream.toXML(textMessageEntity);

            PrintWriter out=response.getWriter();
            out.write(xml);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 回复图片消息
     * @param data
     * @return
     */
    public static String reImage(HttpServletResponse response, Map<String,Object> data){
        try {
            xStream.alias("xml",ImageMessageEntity.class);
            xStream.alias("Image",Image.class);
            ImageMessageEntity imageMessageEntity=new ImageMessageEntity();
            imageMessageEntity.setToUserName(data.get("FromUserName").toString());
            imageMessageEntity.setCreateTime(new Date().getTime());
            imageMessageEntity.setFromUserName(data.get("ToUserName").toString());
            imageMessageEntity.setMsgType("image");
            imageMessageEntity.setImage(new Image(data.get("MediaId").toString()));
            String xml=  xStream.toXML(imageMessageEntity);
            PrintWriter out=response.getWriter();
            out.write(xml);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 回复音乐消息
     * @param data
     * @return
     */
    public static String reMusic(HttpServletResponse response, Map<String,Object> data,String content){
        try {
            xStream.alias("xml",TextMessageEntity.class);
            MusicMessageEntity musicMessageEntity=new MusicMessageEntity();
            musicMessageEntity.setToUserName(data.get("FromUserName").toString());
            musicMessageEntity.setCreateTime(new Date().getTime());
            musicMessageEntity.setFromUserName(data.get("ToUserName").toString());
            musicMessageEntity.setMsgType("music");
            Music music=new Music();
           /* music.setTitle();
            music.setDescription();
            music.setMusicUrl();
            music.setHQMusicUrl();*/
            musicMessageEntity.setMusic(music);

            String xml=  xStream.toXML(musicMessageEntity);

            PrintWriter out=response.getWriter();
            out.write(xml);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 回复图文消息
     * @param data
     * @return
     */
    public static String reTuWen(HttpServletResponse response, Map<String,Object> data){
        try {
            xStream.alias("xml",ArticlesEntity.class);
            ArticlesEntity articlesEntity=new ArticlesEntity();
            articlesEntity.setToUserName(data.get("FromUserName").toString());
            articlesEntity.setCreateTime(new Date().getTime());
            articlesEntity.setFromUserName(data.get("ToUserName").toString());
            articlesEntity.setMsgType("news");
            articlesEntity.setArticleCount(1);
            List<ItemEntity> items=new ArrayList<>();
            xStream.alias("item",ItemEntity.class);
            ItemEntity items1=new ItemEntity("源自家","创建人与家的连接！","http://www.allonhome.com/images/banner2.png","http://www.allonhome.com/");
            items.add(items1);
            articlesEntity.setArticles(items);
            String xml=  xStream.toXML(articlesEntity);
            PrintWriter out=response.getWriter();
            out.write(xml);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 处理CDATA转译
     */
    private  static XStream xStream=new XStream(new XppDriver(){
        public HierarchicalStreamWriter createWriter(Writer out){
            return new PrettyPrintWriter(out){
                boolean cdata=true;
                public void startNode(String name,Class clazz){
                    super.startNode(name,clazz);
                }

                @Override
                protected void writeText(QuickWriter writer, String text) {
                    if(cdata){
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    }else {
                        writer.write(text);
                    }
                }
            };
        }
    });

    /**
     * 获取AccessToken
     */
    public static String getToken() {
        try {
            if(token==null||Expire_time<new Date().getTime()){
                String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
                url= url.replaceAll("APPID",APPID);
                url= url.replaceAll("APPSECRET",APPSECRET);
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
     * 创建自定义菜单
     * @param
     * @return
     */
    public static String createMenu(){
        try {

            String url="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
            String token=getToken();
            url=url.replaceAll("ACCESS_TOKEN",token);

            //最外层
            Map<String,Object> oneMenu=new HashMap<>();
            //一级菜单集合
            List<Map<String,Object>> button=new ArrayList<>();


            //第一个一级菜单
            Map<String,Object> one1=new HashMap<>();
            one1.put("type","click");
            one1.put("name","房源");
            one1.put("key","V1011_TODAY_MUSIC");



            //第二个一级菜单
            Map<String,Object> one2=new HashMap<>();
            one2.put("name","菜单");
            //第二个一级菜单的二级菜单集合
            List<Map<String,Object>> twosub_button=new ArrayList<>();
            //第一个二级菜单
            Map<String,Object> twofirst=new HashMap<>();
            twofirst.put("type","view");
            twofirst.put("name","搜索");
            twofirst.put("url","http://www.baidu.com/");
            //第二个二级菜单
            Map<String,Object> twosecend=new HashMap<>();
            twosecend.put("type","click");
            twosecend.put("name","赞一下我们");
            twosecend.put("key","V1001_GOOD");
            //将所有第二个一级菜单的二级菜单放进第二个一级菜单里
            twosub_button.add(twofirst);
            twosub_button.add(twosecend);
            one2.put("sub_button",twosub_button);



            //第三个一级菜单
            Map<String,Object> one3=new HashMap<>();
            one3.put("name","发图");
            //第三个一级菜单的二级菜单集合
            List<Map<String,Object>> thresub_button=new ArrayList<>();
            //第一个二级菜单
            Map<String,Object> threefirst=new HashMap<>();
            threefirst.put("type","pic_sysphoto");
            threefirst.put("name","系统拍照发图");
            threefirst.put("key","rselfmenu_1_0");
            threefirst.put("sub_button",new ArrayList<>());
            //第二个二级菜单
            Map<String,Object> threesecend=new HashMap<>();
            threesecend.put("type","pic_photo_or_album");
            threesecend.put("name","拍照或者相册发图");
            threesecend.put("key","rselfmenu_1_1");
            threesecend.put("sub_button",new ArrayList<>());
            //第三个二级菜单
            Map<String,Object> threethree=new HashMap<>();
            threethree.put("type","pic_weixin");
            threethree.put("name","微信相册发图");
            threethree.put("key","rselfmenu_1_2");
            threethree.put("sub_button",new ArrayList<>());
            //将所有第三个一级菜单的二级菜单放进第三个一级菜单里
            thresub_button.add(threefirst);
            thresub_button.add(threesecend);
            thresub_button.add(threethree);
            one3.put("sub_button",thresub_button);

            //将所有一级菜单装起来
            button.add(one1);
            button.add(one2);
            button.add(one3);
            oneMenu.put("button",button);

            org.json.JSONObject jsonObject=new org.json.JSONObject(oneMenu);

            String result= HttpClientUtil.doPostJson(url,jsonObject.toString());
            return  result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置行业模板
     * @param
     * @return
     */
    public static String setTempelete(){
        try {

            String url="https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";
            String token=getToken();
            url=url.replaceAll("ACCESS_TOKEN",token);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("industry_id1",31);
            jsonObject.put("industry_id2",41);
           String result= HttpClientUtil.doPostJson(url,jsonObject.toJSONString());
           return  result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取行业模板
     * @param
     * @return
     */
    public static String getTempelete(){
        try {

            String url="https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=ACCESS_TOKEN";
            String token=getToken();
            url=url.replaceAll("ACCESS_TOKEN",token);

            String result= HttpClientUtil.doGet(url);
            return  result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送模板消息
     * @param
     * @return
     */
    public static String sendTempeleteMessage(){
        try {

            String url="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
            String token=getToken();
            url=url.replaceAll("ACCESS_TOKEN",token);

            Map<String,Object> map=new HashMap<>();

            map.put("touser","oSm7K1U7yEp1TWLQv_Jj3wnTrjPY");
            map.put("template_id","N_JWNIQoYIvPi4OfFKGCEMP-LTKAEua4jsKRve9mFUs");
            map.put("url","http://www.baidu.com/");
           /* Map<String,Object> miniprogram=new HashMap<>();
            miniprogram.put("appid",APPID);
            map.put("miniprogram",miniprogram);*/

            Map<String,Object> data=new HashMap<>();

            Map<String,Object> first=new HashMap<>();
            first.put("value","尊敬的客户，您的订单已支付成功");
            first.put("color","#173177");
            data.put("first",first);
            Map<String,Object> keyword1=new HashMap<>();
            keyword1.put("value","2014款背包");
            keyword1.put("color","#173177");
            data.put("keyword1",keyword1);
            Map<String,Object> keyword2=new HashMap<>();
            keyword2.put("value","201500001");
            keyword2.put("color","#173177");
            data.put("keyword2",keyword2);
            Map<String,Object> keyword3=new HashMap<>();
            keyword3.put("value","150元");
            keyword3.put("color","#173177");
            data.put("keyword3",keyword3);
            Map<String,Object> keyword4=new HashMap<>();
            keyword4.put("value","2014年10月21日13：00");
            keyword4.put("color","#173177");
            data.put("keyword4",keyword4);
            Map<String,Object> remark=new HashMap<>();
            remark.put("value","感谢您的光临");
            remark.put("color","#173177");
            data.put("remark",remark);
            map.put("data",data);

            org.json.JSONObject jsonObject=new org.json.JSONObject(map);
            String result= HttpClientUtil.doPostJson(url,jsonObject.toString());
            return  result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 上传临时素材
     * @param fileType
     * @param filePath
     * @return
     * @throws Exception
     */
    public static org.json.JSONObject UploadMeida(String fileType, String filePath) throws Exception{
        //返回结果
        String result=null;
        File file=new File(filePath);
        if(!file.exists()||!file.isFile()){
            throw new IOException("文件不存在");
        }
        String token=getToken();
        String urlString="https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
        urlString=urlString.replaceAll("ACCESS_TOKEN",token).replaceAll("TYPE",fileType);
        URL url=new URL(urlString);
        HttpsURLConnection conn=(HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("POST");//以POST方式提交表单
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);//POST方式不能使用缓存
        //设置请求头信息
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", "UTF-8");
        //设置边界
        String BOUNDARY="----------"+System.currentTimeMillis();
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        //请求正文信息
        //第一部分
        StringBuilder sb=new StringBuilder();
        sb.append("--");//必须多两条道
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"media\"; filename=\"" + file.getName()+"\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        System.out.println("sb:"+sb);

        //获得输出流
        OutputStream out=new DataOutputStream(conn.getOutputStream());
        //输出表头
        out.write(sb.toString().getBytes("UTF-8"));
        //文件正文部分
        //把文件以流的方式 推送道URL中
        DataInputStream din=new DataInputStream(new FileInputStream(file));
        int bytes=0;
        byte[] buffer=new byte[1024];
        while((bytes=din.read(buffer))!=-1){
            out.write(buffer,0,bytes);
        }
        din.close();
        //结尾部分
        byte[] foot=("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");//定义数据最后分割线
        out.write(foot);
        out.flush();
        out.close();
        if(HttpsURLConnection.HTTP_OK==conn.getResponseCode()){

            StringBuffer strbuffer=null;
            BufferedReader reader=null;
            try {
                strbuffer=new StringBuffer();
                reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String lineString=null;
                while((lineString=reader.readLine())!=null){
                    strbuffer.append(lineString);

                }
                if(result==null){
                    result=strbuffer.toString();
                    System.out.println("result:"+result);
                }
            } catch (IOException e) {
                System.out.println("发送POST请求出现异常！"+e);
                e.printStackTrace();
            }finally{
                if(reader!=null){
                    reader.close();
                }
            }

        }
        org.json.JSONObject jsonObject=new org.json.JSONObject(result);
        return jsonObject;
    }

    /**
     * 获取临时素材
     * @param
     * @return
     */
    public static String getMeida(String MEDIA_ID){
        try {

            String url="https://api.weixin.qq.com/cgi-bin/media/get?access_token=20_0kAtMUuiXAIEWnVtv4Q69Oj5_fZ034LfJQ2C8u7mMcaP6_807IdtgaAw3Or4WfJoGtr4MRfHigbnuaFcYBbptzA0z5zr26-7KFWoKvTYty9PbkJIkgLAPz6Ob1d2UXsT37zTj77cbSTcZW4XAIDiAFANVQ&media_id=bKZSpd3qPycDQ3mQmZH5PDRjShtej-KcuyDTohPni9cdBOHovjQv2FWO7B847h16";
            String token=getToken();
          /*  String MEDIA_ID="LNPxTjg2inVmei53KfljXlnvsH1iA5CLCQoff493z8L6OLxUBlufRSw-H6WwFBm8";*/
            url=url.replaceAll("ACCESS_TOKEN",token).replaceAll("MEDIA_ID",MEDIA_ID);
            String result= HttpClientUtil.doGet(url);
            return  result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 通过code换取网页授权access_token
     * @param
     * @return
     */
    public static String getAccess_token(String appid,String secret,String code){
        try {
            String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
            url=url.replaceAll("APPID",appid).replaceAll("SECRET",secret).replaceAll("CODE",code);
            String result= HttpClientUtil.doGet(url);
            return  result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 拉取用户信息
     * @param
     * @return
     */
    public static String getUserinfo(String access_token,String openid){
        try {
            String url="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
            url=url.replaceAll("ACCESS_TOKEN",access_token).replaceAll("OPENID",openid);
            String result= HttpClientUtil.doGet(url);
            return  result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}
