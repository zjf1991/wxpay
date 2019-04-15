package com.yzj.wxpay.controller.alipaycontroller;


import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.yzj.wxpay.utils.alipay.AlipayConfig;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * 支付宝退款
 */
@RestController
public class AliRefundController {


    /**
     * 支付宝退款
     * @return
     */
    @RequestMapping(value = "/app/api/v1/pay/alipay/refund", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    public boolean pay(@RequestBody Map<String,Object> paload, HttpServletResponse response1){

        String out_trade_no=paload.get("out_trade_no").toString();
        String rechargeMoney=paload.get("rechargeMoney").toString();

        try {
            //实例化客户端
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APP_ID, AlipayConfig.APP_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGN_TYPE);
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            Map<String, Object> params = new HashMap<>();
            params.put("out_trade_no", out_trade_no);
            params.put("refund_amount", rechargeMoney);

            JSONObject jsonObject=new JSONObject(params);
            String BizContent= jsonObject.toString();
            request.setBizContent(BizContent);
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                System.out.println("调用成功");
                String responseBody = response.getBody();
                JSONObject rejsonObject = new JSONObject(responseBody);
                String code=rejsonObject.getJSONObject("alipay_trade_refund_response").getString("code");
                if(code.equals("10000")){
                   /* String msg=rejsonObject.getJSONObject("alipay_trade_refund_response").getString("msg");*/
                    return true;
                }else {
                    /*String sub_msg = rejsonObject.getJSONObject("alipay_trade_refund_response").getString("sub_msg");*/
                    return false;
                }
            } else {
                /*String responseBody = response.getBody();
                JSONObject rejsonObject = new JSONObject(responseBody);
                String sub_msg = rejsonObject.getJSONObject("alipay_trade_refund_response").getString("sub_msg");
                System.out.println(sub_msg);*/
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}



