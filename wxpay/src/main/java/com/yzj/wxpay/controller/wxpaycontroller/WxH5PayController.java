package com.yzj.wxpay.controller.wxpaycontroller;


import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.yzj.wxpay.utils.RetJson;
import com.yzj.wxpay.utils.wxpay.HttpClientUtil;
import com.yzj.wxpay.utils.wxpay.WXPayConfigImpl;
import com.yzj.wxpay.utils.wxpay.WXpayConfig;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信H5支付
 */

@RestController
public class WxH5PayController {

    /**
     * H5支付
     * @param response
     * @param paload
     * @return
     */
    @RequestMapping("/pay/wx/h5pay/satrt")
    public RetJson getCode(HttpServletResponse response,@RequestBody Map<String,Object> paload){
        RetJson retJson=new RetJson();
        try {
          /*  String out_trade_no=paload.get("out_trade_no").toString();*/
            /*String rechargeMoney=paload.get("rechargeMoney").toString();*/
            String out_trade_no=WXPayUtil.generateNonceStr();
            String rechargeMoney="0.01";
            String cip=paload.get("cip").toString();
            String body="智能锁支付";
            //调用统一下单接口
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("body", body);
            data.put("out_trade_no", out_trade_no);
            data.put("appid",WXpayConfig.APPID);
            data.put("mch_id",WXpayConfig.MchID);
            data.put("nonce_str",WXPayUtil.generateNonceStr());
            data.put("device_info", "WEB");
            data.put("fee_type", "CNY");

            DecimalFormat df   = new DecimalFormat("######0.00");
            String m = rechargeMoney;
            m = df.format((Double.valueOf(m)* 100));
            data.put("total_fee", String.valueOf(new Double(m).intValue()));
            System.out.println(cip);
            data.put("spbill_create_ip", cip);
            data.put("notify_url", WXpayConfig.FORMALSERVERADDRESS+"/app/api/wx/h5/notify");
            data.put("trade_type", "MWEB ");
            data.put("product_id", out_trade_no);
            HashMap<String, Object> scene_info = new HashMap<String, Object>();
            HashMap<String, String> h5_info = new HashMap<String, String>();
            h5_info.put("type","Wap");
            h5_info.put("wap_url","https://pay.qq.com");
            h5_info.put("wap_name","腾讯充值");
            scene_info.put("h5_info",h5_info);
            JSONObject jsonObject=new JSONObject(scene_info);
            data.put("scene_info",jsonObject.toString());
            //签名
            String sign=  WXPayUtil.generateSignature(data,WXpayConfig.Key);
            data.put("sign", sign);
            //将类型为map的参数转换为xml
            String requestxml= WXPayUtil.mapToXml(data);
            String responseDataXml=  HttpClientUtil.doPostByXml(WXpayConfig.UNIFIEDORDER_URL_SUFFIX,requestxml);
            //将响应值转换成map
            Map<String, String> responseDataMap=  WXPayUtil.xmlToMap(responseDataXml);
            String return_code =responseDataMap.get("return_code");
            if(return_code.equals("SUCCESS")){
                String result_code =responseDataMap.get("result_code");
                if(result_code.equals("SUCCESS")){
                    String mweb_url =responseDataMap.get("mweb_url");
                    retJson.ret=0;
                    retJson.msg="success";
                    retJson.data=mweb_url;
                   /* response.sendRedirect(mweb_url);*/
                    return retJson;
                }else {
                    String err_code_des =responseDataMap.get("err_code_des");
                    retJson.ret=1;
                    retJson.msg="fail";
                    retJson.data=err_code_des;
                    return  retJson;
                }
            }else {
                retJson.ret=1;
                retJson.msg="fail";
                return  retJson;
            }
        }  catch (Exception e){
            e.printStackTrace();
            retJson.ret=1;
            retJson.msg="fail";
            retJson.data=e;
            return  retJson;
        }
    }

    /**
     * 处理微信支付结果通知
     *
     * @param
     *
     * @return
     * @throws Exception
     * @throws
     * @throws
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/app/api/wx/h5/notify", method = RequestMethod.POST)
    public void getnotify(HttpServletRequest request,
                          HttpServletResponse response) throws Exception {

        System.out.println("ALI回调");
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        String result = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> map = null;
        boolean flag = true;
        try {
            /**
             * 解析微信通知返回的信息
             */
            map = WXPayUtil.xmlToMap(result);
            System.out.println(map.toString());
            WXPayConfigImpl config = WXPayConfigImpl.getInstance();
            WXPay wxpay = new WXPay(config);
            flag = wxpay.isPayResultNotifySignatureValid(map);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.err.println(flag);
        String resXml = "";
        // 若支付成功，则告知微信服务器收到通知
        if (flag) {
            if (map.get("return_code").equals("SUCCESS")) {
                if (map.get("result_code").equals("SUCCESS")) {


                    System.out.println("成功");
                    resXml = "<xml>"
                            + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![CDATA[OK]]></return_msg>"
                            + "</xml> ";
                }else {
                    resXml = "<xml>"
                            + "<return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[报文为空]]></return_msg>"
                            + "</xml> ";
                    System.err.println(map.get("err_code") + ":"
                            + map.get("err_code_des"));
                }
            } else {
                System.out.println("通信失败！");
                resXml = "<xml>"
                        + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>"
                        + "</xml> ";

            }
        }
        // 处理业务完毕
        // ------------------------------
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

}
