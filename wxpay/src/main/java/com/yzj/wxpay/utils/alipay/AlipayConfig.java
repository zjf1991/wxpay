package com.yzj.wxpay.utils.alipay;


import com.yzj.wxpay.utils.wxpay.WXpayConfig;

/**
 *
 */
public class AlipayConfig {

	/**
	 * 商品的标题
	 */
	public static final String COMMODITY_SUBJECT = "";

	/**
	 * 支付宝网关（固定）
	 */
	public static final String URL = "https://openapi.alipay.com/gateway.do";

	/**
	 * APPID即创建应用后生成
	 */
	public static String APP_ID = "afsadgsdgsdhhsdfh";

	/**
	 * 卖家email
	 */
	public static String SELLER_ID = "ahadfhadfjhadfjdafjdafj";

	/**
	 * 开发者应用私钥，由开发者自己生成
	 */
	public static final String APP_PRIVATE_KEY = "dhfgsdfhsdjjjjjgjsfsfgjkhfsfsjgfsgjfsjggjsafgjfrgjfsjX2hLhbN+DS0MuugfZTZP6jOfLEiIO2EwPYUSRF6cy9qS4celDAawPjvpNJLvcU1YZC9RznT8oM0gDqZWIvcsYmVfgN1LVlfx2f/ccKSs8RwJs73vA/CEgUZQhikTdLHnbTpiuTM+qcXq7CsEmeQKBgQD7ICk3zUgNGBHa05rlPBWQaztNf37SouWSyeLSxuok33L7AHtIbMRBVm2wZvDbQL8YoBgO5bfOIjgldEgRPLXpGD2p6Wv9rhOeAL+5+C1lXy7XCvSwpV37VN8pswqQnVa8z9uG/8Y+lUOE38PqKQ0JW0pb75Zstu05QGUsCPlVdwKBgQDPO05HBPFe4Rk6ebb5x/Rd8WKqsigbvtJGjxJE6I47R21Gt1y4/zlaVStcQRlHRJ5hFDXMqvJZZ9bZwe8InaP7FpLdNNUyPepTKxblAn7QAfjPoRaTvHcRa0+UCy/nQ49dvgOtrVqkZjzZqb6+gYmG78ZxysYqLYyayvJ3yHLuVQKBgB+V7PlCzaQv+dbvNJVOmFvA0QWvWpE9wehTkj72m+6lWD3v0pHKdGjPaR595B9/pkl/oWcGR7caoLs9Q+8/FpZJ4T+kx1tx8K+34mWOrI8KiMatbxdkxUxeYW9KQLLtx0IaVr9FHncyqPpmdqrWHPinTdQAb4ge/flsew0t18aDAoGBAKF5OqNIDLO7fib8VEEJNzZEMneDXpFP/RURwlCDTOCP5Wwm0E8uQAhNOP4nk4TeyPIoB0u1ICXeLaF75b+25eFuU32d/0moyP+JIcvMR/fYsYFHqgnv3m9qSJW4qdjv4bRPrRoVa/2R1HReOczDrt+/0cc1E3gmCdtLq+b9/+AJAoGANdOARnqAFYf6jwGYnvOgW8L1g6RS/GHY6PE17OupYAntb3j9G8KEXK/6lg0AK9X7KWntOXG4ZSkJ4aJdSUdibPcpbhtwdojDpTZb4aLDhM4h2MOEK+OM8Mi7/XSBYA6YaIclY2x5OZRbBhu+fC2j/1B2fMeduyZ83fdpo6mIjBU=";

	/**
	 * 方式
	 */
	public static final String TRADE_PAY_METHOD = "alipay.trade.app.pay";

	/**
	 * 版本
	 */
	public static final String VERSION = "1.0";

	/**
	 * 参数返回格式，只支持json
	 */
	public static final String FORMAT = "json";

	/**
	 * 请求和签名使用的字符编码格式，支持GBK和UTF-8
	 */
	public static final String CHARSET = "UTF-8";

	/**
	 * 支付宝公钥，由支付宝生成
	 */
	public static String ALIPAY_PUBLIC_KEY = "aerhearhaerhaerjhrrtjytyAriiOeYQ7QEaWr+3Ph1SPPPRZVb6+rBTHuRI7qA4jrtjrjrtjrtjrtjrtjrtjrtjrtjratjartjart"
	/**
	 * 商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
	 */
	public static final String SIGN_TYPE = "RSA2";

	/**
	 * 支付宝异步通知地址
	 */
	public static String ALIPAY_NOTIFY_URL = WXpayConfig.FORMALSERVERADDRESS+"/app/api/ali/notify";

	/**
	 * 测试支付宝异步通知地址
	 */
	public static String ALIPAY_NOTIFY_URL_test = WXpayConfig.FORMALSERVERADDRESS+"/app/api/ali/notify";
	// 快递费用通知地址
	public static String ALIPAY_EXPRESS_NOTIFY_URL = WXpayConfig.FORMALSERVERADDRESS+"/app/api/ali/notify/express";
	public static String ALIPAY_EXPRESS_NOTIFY_URL_test = WXpayConfig.FORMALSERVERADDRESS+"/app/api/ali/notify/express";
	
	// 快递费用通知地址
		public static String ALIPAY_SMARTLOCK_NOTIFY_URL = WXpayConfig.FORMALSERVERADDRESS+"/app/api/ali/notify/smartlockrecord";
		public static String ALIPAY_SMARTLOCK_NOTIFY_URL_test = WXpayConfig.FORMALSERVERADDRESS+"/app/api/ali/notify/smartlockrecord";

	/**
	 * 支付超时时间
	 */
	public static String TIMEOUT_EXPRESS = "1d";

	/**
	 * 可支付的渠道
	 */
	public static String ENABLE_PAY_CHANNELS = "";

	/**
	 * 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
	 */
	public static final String PRODUCT_CODE = "QUICK_MSECURITY_PAY";
}
