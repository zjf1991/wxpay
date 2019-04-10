package com.yzj.wxpay.controller.alipaycontroller;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.yzj.wxpay.utils.QRcode;
import com.yzj.wxpay.utils.alipay.AlipayConfig;
import com.yzj.wxpay.utils.wxpay.WXpayConfig;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * 支付宝扫码支付
 */
@RestController
public class AliPayController {

    /**
     * 统一下单
      * @return
     */
    @RequestMapping(value = "/app/api/v1/pay/alipay", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    public String pay(@RequestBody Map<String,Object> paload, HttpServletResponse response1){

        String out_trade_no=paload.get("out_trade_no").toString();
        String rechargeMoney=paload.get("rechargeMoney").toString();
        String body=paload.get("body").toString();
        try {
            //实例化客户端
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APP_ID, AlipayConfig.APP_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGN_TYPE);
            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
            Map<String, Object> params = new HashMap<>();
            params.put("out_trade_no", out_trade_no);
            params.put("total_amount", rechargeMoney);
            params.put("subject", "源自家");
            params.put("body", body);
            JSONObject jsonObject=new JSONObject(params);
            String BizContent= jsonObject.toString();
            request.setBizContent(BizContent);
            request.setNotifyUrl(WXpayConfig.FORMALSERVERADDRESS+"/app/api/ali/notify");
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                System.out.println("调用成功");
                String responseBody = response.getBody();
                JSONObject rejsonObject = new JSONObject(responseBody);
                String code=rejsonObject.getJSONObject("alipay_trade_precreate_response").getString("code");
                if(code.equals("10000")){
                    String qr_code = rejsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
                    System.out.println(qr_code);
                    QRcode.getQRcode(qr_code,response1);
                    return qr_code;
                }else {
                    String sub_msg = rejsonObject.getJSONObject("alipay_trade_precreate_response").getString("sub_msg");
                    System.out.println(sub_msg);
                    return sub_msg;
                }
            } else {
                String responseBody = response.getBody();
                JSONObject rejsonObject = new JSONObject(responseBody);
                String sub_msg = rejsonObject.getJSONObject("alipay_trade_precreate_response").getString("sub_msg");
                System.out.println(sub_msg);
                return sub_msg;
            }
        }catch (Exception e){
            e.printStackTrace();
            return "调用失败！";
        }

    }


    /**
     * 支付宝扫码支付成功回调
     *
     * @return
     * @throws Exception
     * @throws AlipayApiException
     */
    @RequestMapping(value = "/app/api/ali/notify", method = RequestMethod.POST)
    public String Notify(HttpServletRequest request)
            throws Exception {
        System.out.println("ALI回调");
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();

        for (Iterator<String> iter = requestParams.keySet().iterator(); iter
                .hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params,
                    AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET,
                    "RSA2");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        // 调用SDK验证签名
        if (signVerified) {
            String out_trade_no = request.getParameter("out_trade_no");
            String tradeStatus = request.getParameter("trade_status");
            String trade_no = request.getParameter("trade_no");

            System.out.println("支付宝订单id----------" + out_trade_no + "-------------");
            System.out.println("支付宝订单号----------" + trade_no + "-------------");
            if (tradeStatus.equals("TRADE_FINISHED")
                    || tradeStatus.equals("TRADE_SUCCESS")) {
                System.out.println("回调成功");

            } else {
                return "fail";
            }
        } else {
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            //System.out.println("验证失败,不去更新状态");
            System.out.println("验证失败,不去更新状态");
            return "fail";
        }
        return null;
    };

}
