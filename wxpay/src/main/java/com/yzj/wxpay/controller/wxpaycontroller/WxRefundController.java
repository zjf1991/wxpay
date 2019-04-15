package com.yzj.wxpay.controller.wxpaycontroller;


import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.yzj.wxpay.utils.wxpay.WXPayConfigImpl;
import com.yzj.wxpay.utils.wxpay.WXpayConfig;
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
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * 微信退款
 */
@RestController
public class WxRefundController {

    @RequestMapping("/wx/pay/refund/satart")
    public boolean wxRefund( @RequestBody Map<String,Object> paload){
        try {
            String out_trade_no=paload.get("out_trade_no").toString();
            String rechargeMoney=paload.get("rechargeMoney").toString();
            //调用统一下单接口
            SortedMap<String, String> data = new TreeMap<>();

            data.put("appid",WXpayConfig.APPID);
            data.put("mch_id",WXpayConfig.MchID);
            data.put("nonce_str",WXPayUtil.generateNonceStr());
            data.put("out_trade_no", out_trade_no);
            data.put("out_refund_no", WXPayUtil.generateNonceStr());
            data.put("fee_type", "CNY");
            DecimalFormat df   = new DecimalFormat("######0.00");
            String m = rechargeMoney;
            m = df.format((Double.valueOf(m)* 100));
            data.put("total_fee", String.valueOf(new Double(m).intValue()));
            data.put("refund_fee", String.valueOf(new Double(m).intValue()));
            //签名
            String sign=  WXPayUtil.generateSignature(data,WXpayConfig.Key);
            data.put("sign", sign);
            //将类型为map的参数转换为xml
            String requestxml= WXPayUtil.mapToXml(data);
            String responseDataXml=  WXpayConfig.wxPayBack(WXpayConfig.REFUND_URL,requestxml);
            //将响应值转换成map
            Map<String, String> responseDataMap=  WXPayUtil.xmlToMap(responseDataXml);
            String return_code =responseDataMap.get("return_code");
            if(return_code.equals("SUCCESS")){
                String result_code =responseDataMap.get("result_code");
                if(result_code.equals("SUCCESS")){
                    return true;
                }else {
                    String err_code_des =responseDataMap.get("err_code_des");
                    return  false;
                }
            }else {
                String err_code_des = responseDataMap.get("return_msg");
                return false ;
            }

        }  catch (Exception e){
            e.printStackTrace();
            return false;
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
    @RequestMapping(value = "/app/api/wx/refund/notify", method = RequestMethod.POST)
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
