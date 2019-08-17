package com.yzj.wxpay.utils.wxpay;

import com.github.wxpay.sdk.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class WXPayConfigImpl implements WXPayConfig {

	private byte[] certData;
	private static WXPayConfigImpl INSTANCE;

	private WXPayConfigImpl() throws Exception {
		/*
		 * String certPath = "D://CERT/common/apiclient_cert.p12"; File file =
		 * new File(certPath); InputStream certStream = new
		 * FileInputStream(file); this.certData = new byte[(int) file.length()];
		 * certStream.read(this.certData); certStream.close();
		 */
	}

	public static WXPayConfigImpl getInstance() throws Exception {
		if (INSTANCE == null) {
			synchronized (WXPayConfigImpl.class) {
				if (INSTANCE == null) {
					INSTANCE = new WXPayConfigImpl();
				}
			}
		}
		return INSTANCE;
	}

	public String getAppID() {
		return "sdagasdfgagasdgad";
	}

	public String getMchID() {
		return "adfhdafhdsfhdsfh";
	}

	public String getKey() {
		return "dafhdafhadfhadfhdafhadfh";
	}

	public InputStream getCertStream() {
		ByteArrayInputStream certBis;
		certBis = new ByteArrayInputStream(this.certData);
		return certBis;
	}

	public int getHttpConnectTimeoutMs() {
		return 2000;
	}

	public int getHttpReadTimeoutMs() {
		return 10000;
	}

/*	IWXPayDomain getWXPayDomain() {
		return WXPayDomainSimpleImpl.instance();
	}*/

	public String getPrimaryDomain() {
		return "api.mch.weixin.qq.com";
	}

	public String getAlternateDomain() {
		return "api2.mch.weixin.qq.com";
	}

/*	@Override
	public int getReportWorkerNum() {
		return 1;
	}

	@Override
	public int getReportBatchSize() {
		return 2;
	}*/
}
